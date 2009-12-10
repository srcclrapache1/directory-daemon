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
package org.apache.directory.daemon;


import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Convenience class to encapsulate paths to various folders and files within
 * an installation.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class InstallLayout
{
    private final static Logger log = LoggerFactory.getLogger( InstallLayout.class );
    private final static FileFilter JAR_FILTER;

    static
    {
        JAR_FILTER = new FileFilter()
        {
            public boolean accept( File pathname )
            {
                return pathname.isFile() && pathname.getName().endsWith( ".jar" );
            }
        };
    }

    protected final File installHomeDir;
    private transient File[] dirs;
    private transient File[] files;
    private transient URL[] allJars = null;
    private transient URL[] dependentJars = null;
    private transient URL[] extensionJars = null;


    public InstallLayout( File installHomeDir )
    {
        this.installHomeDir = installHomeDir;
    }


    public File getInstallHomeDir()
    {
        return installHomeDir;
    }


    public File getBinDirectory()
    {
        return new File( installHomeDir, "bin" );
    }


    public File getLibDirectory()
    {
        return new File( installHomeDir, "lib" );
    }


    public File getBootstrapper()
    {
        return new File( getLibDirectory(), "bootstrapper.jar" );
    }


    public File getLogger()
    {
        return new File( getLibDirectory(), "logger.jar" );
    }


    public File getDaemon()
    {
        return new File( getLibDirectory(), "wrapper.jar" );
    }


    public File getInitScript()
    {
        return getInitScript( "server.init" );
    }


    public File getInitScript( String name )
    {
        return new File( getBinDirectory(), name );
    }


    public File getExtensionsDirectory()
    {
        return new File( getLibDirectory(), "ext" );
    }


    public File getConfigurationDirectory()
    {
        return new File( installHomeDir, "conf" );
    }


    public File getConfigurationFile()
    {
        return getConfigurationFile( "server.xml" );
    }


    public File getConfigurationFile( String name )
    {
        return new File( getConfigurationDirectory(), name );
    }


    public File getLogoIconFile()
    {
        return getLogoIconFile( "logo.ico" );
    }


    public File getLogoIconFile( String name )
    {
        return new File( getInstallHomeDir(), name );
    }


    public File getLicenseFile()
    {
        return getLicenseFile( "LICENSE" );
    }


    public File getLicenseFile( String name )
    {
        return new File( getInstallHomeDir(), name );
    }


    public File getReadmeFile()
    {
        return getReadmeFile( "README" );
    }


    public File getReadmeFile( String name )
    {
        return new File( getInstallHomeDir(), name );
    }


    public File getBootstrapperConfigurationFile()
    {
        return new File( getConfigurationDirectory(), "apacheds.conf" );
    }


    public File getLoggerConfigurationFile()
    {
        return new File( getConfigurationDirectory(), "log4j.properties" );
    }


    public void init()
    {
        if ( dirs == null )
        {
            dirs = new File[]
                { 
                getInstallHomeDir(), 
                getBinDirectory(), 
                getLibDirectory(),
                getExtensionsDirectory(), 
                getConfigurationDirectory(), 
                };
        }

        if ( files == null )
        {
            // only these files are required to be present
            files = new File[]
                { 
                getBootstrapper(), 
                getBootstrapperConfigurationFile() 
                };
        }
    }


    public void verifyInstallation()
    {
        init();

        for ( File dir:dirs )
        {
            if ( !dir.exists() )
            {
                throw new IllegalStateException( dir + " does not exist!" );
            }

            if ( dir.isFile() )
            {
                throw new IllegalStateException( dir + " is a file when it should be a directory." );
            }

            if ( !dir.canWrite() )
            {
                throw new IllegalStateException( dir + " is write protected from the current user: "
                    + System.getProperty( "user.name" ) );
            }
        }

        for ( File file:files )
        {
            if ( !file.exists() )
            {
                throw new IllegalStateException( file + " does not exist!" );
            }

            if ( file.isDirectory() )
            {
                throw new IllegalStateException( file + " is a directory when it should be a file." );
            }

            if ( !file.canRead() )
            {
                throw new IllegalStateException( file + " is not readable by the current user: "
                    + System.getProperty( "user.name" ) );
            }
        }
    }


    public void mkdirs()
    {
        init();

        for ( int ii = 0; ii < dirs.length; ii++ )
        {
            dirs[ii].mkdirs();
        }
    }


    public URL[] getDependentJars()
    {
        if ( dependentJars == null )
        {
            File[] deps = getLibDirectory().listFiles( new FileFilter()
            {
                public boolean accept( File pathname )
                {
                    return pathname.isFile() && pathname.getName().endsWith( ".jar" );
                }
            } );

            dependentJars = new URL[deps.length];
            for ( int ii = 0; ii < deps.length; ii++ )
            {
                try
                {
                    dependentJars[ii] = deps[ii].toURI().toURL();
                }
                catch ( MalformedURLException e )
                {
                    log.error( "Failed to generate a URL for " + deps[ii]
                        + ".  It will not be added to the dependencies." );
                }
            }
        }

        return dependentJars;
    }


    public URL[] getExtensionJars()
    {
        if ( extensionJars == null )
        {
            File[] extensions = getExtensionsDirectory().listFiles( JAR_FILTER );

            extensionJars = new URL[extensions.length];
            for ( int ii = 0; ii < extensions.length; ii++ )
            {
                try
                {
                    extensionJars[ii] = extensions[ii].toURI().toURL();
                }
                catch ( MalformedURLException e )
                {
                    log.error( "Failed to generate a URL for " + extensions[ii]
                        + ".  It will not be added to the extensions." );
                }
            }
        }

        return extensionJars;
    }


    public URL[] getAllJars()
    {
        if ( allJars == null )
        {
            int dependentLength = getDependentJars().length;
            int extensionLength = getExtensionJars().length;
            allJars = new URL[dependentLength + extensionLength];

            for ( int ii = 0; ii < allJars.length; ii++ )
            {
                if ( ii < dependentLength )
                {
                    allJars[ii] = dependentJars[ii];
                }
                else
                {
                    allJars[ii] = extensionJars[ii - dependentLength];
                }
            }
        }

        return allJars;
    }
}