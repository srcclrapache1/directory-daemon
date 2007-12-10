/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.daemon.installers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.directory.daemon.InstallationLayout;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.taskdefs.Execute;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * Some helper/utility methods for this plugin.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class MojoHelperUtils
{
    public static void copyBinaryFile( InputStream from, File to ) throws IOException
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream( to );
            IOUtil.copy( from, out );
        }
        finally
        {
            IOUtil.close( from );
            IOUtil.close( out );
        }
    }


    public static void copyAsciiFile( ServiceInstallersMojo mymojo, Properties filterProperties, InputStream from,
        File to, boolean filtering ) throws IOException
    {
        // buffer so it isn't reading a byte at a time!
        Reader fileReader = null;
        Writer fileWriter = null;
        try
        {
            if ( mymojo.getEncoding() == null || mymojo.getEncoding().length() < 1 )
            {
                fileReader = new BufferedReader( new InputStreamReader( from ) );
                fileWriter = new FileWriter( to );
            }
            else
            {
                FileOutputStream outstream = new FileOutputStream( to );
                fileReader = new BufferedReader( new InputStreamReader( from, mymojo.getEncoding() ) );
                fileWriter = new OutputStreamWriter( outstream, mymojo.getEncoding() );
            }

            Reader reader = null;
            if ( filtering )
            {
                // support _${token}
                reader = new InterpolationFilterReader( fileReader, filterProperties, "_${", "}" );
                // support ${token}
                reader = new InterpolationFilterReader( reader, filterProperties, "${", "}" );
                // support @token@
                reader = new InterpolationFilterReader( reader, filterProperties, "@", "@" );

                boolean isPropertiesFile = false;
                if ( to.isFile() && to.getName().endsWith( ".properties" ) )
                {
                    isPropertiesFile = true;
                }
                reader = new InterpolationFilterReader( reader, new ReflectionProperties( mymojo.getProject(),
                    isPropertiesFile ), "${", "}" );
            }
            else
            {
                reader = fileReader;
            }
            IOUtil.copy( reader, fileWriter );
        }
        finally
        {
            IOUtil.close( fileReader );
            IOUtil.close( fileWriter );
        }
    }


    public static void copyAsciiFile( ServiceInstallersMojo mymojo, Properties filterProperties, File from, File to,
        boolean filtering ) throws IOException
    {
        copyAsciiFile( mymojo, filterProperties, new FileInputStream( from ), to, filtering );
    }


    public static List copyDependencies( ServiceInstallersMojo mymojo, InstallationLayout layout )
        throws MojoFailureException
    {
        List<Artifact> libArtifacts = new ArrayList<Artifact>();
        Artifact artifact = null;
        List<String> rejects = new ArrayList<String>();

        mymojo.getLog().info( "" );
        mymojo.getLog().info( "    Including artifacts: " );
        mymojo.getLog().info( "    -------------------" );
        Iterator artifacts = mymojo.getProject().getRuntimeArtifacts().iterator();
        
        while ( artifacts.hasNext() )
        {
            artifact = ( Artifact ) artifacts.next();
            String key = artifact.getGroupId() + ":" + artifact.getArtifactId();

            if ( artifact.equals( mymojo.getBootstrapper() ) )
            {
                rejects.add( key );
            }
            else if ( artifact.equals( mymojo.getDaemon() ) )
            {
                rejects.add( key );
            }
            else
            {
                if ( mymojo.getExcludes().contains( key ) )
                {
                    rejects.add( key );
                    continue;
                }

                try
                {
                    FileUtils.copyFileToDirectory( artifact.getFile(), layout.getLibDirectory() );
                    libArtifacts.add( artifact );
                    mymojo.getLog().info( "        o " + key );
                }
                catch ( IOException e )
                {
                    throw new MojoFailureException( "Failed to copy dependency artifact " + artifact
                        + " into position " + layout.getLibDirectory() );
                }
            }
        }

        if ( !mymojo.getExcludes().isEmpty() )
        {
            mymojo.getLog().info( "" );
            mymojo.getLog().info( "    Excluded artifacts: " );
            mymojo.getLog().info( "    ------------------" );
            for ( int ii = 0; ii < rejects.size(); ii++ )
            {
                mymojo.getLog().info( "        o " + rejects.get( ii ) );
            }
        }
        else
        {
            mymojo.getLog().info( "No artifacts have been excluded." );
        }
        mymojo.getLog().info( "" );

        return libArtifacts;
    }


    public static void exec( String[] cmd, File workDir, boolean doSudo ) throws MojoFailureException
    {
        Execute task = new Execute();
        task.setCommandline( cmd );
        task.setSpawn( true );
        task.setWorkingDirectory( workDir );

        if ( doSudo )
        {
            String cmdString = " ";
            for ( int ii = 0; ii < cmd.length; ii++ )
            {
                System.out.println( "cmd[" + ii + "] = " + cmd[ii] );
                cmdString += cmd[ii] + " ";
            }

            String[] temp = new String[2];
            temp[0] = "sudo";
            temp[1] = cmdString;
            cmd = temp;
        }

        String cmdString = " ";
        for ( int ii = 0; ii < cmd.length; ii++ )
        {
            System.out.println( "cmd[" + ii + "] = " + cmd[ii] );
            cmdString += cmd[ii] + " ";
        }

        try
        {
            task.execute();
        }
        catch ( IOException e )
        {
            throw new MojoFailureException( "Failed while trying to execute '" + cmdString + "': " + e.getMessage() );
        }

        if ( task.getExitValue() != 0 )
        {
            throw new MojoFailureException( "Execution of '" + cmdString + "' resulted in a non-zero exit value: "
                + task.getExitValue() );
        }
    }
}
