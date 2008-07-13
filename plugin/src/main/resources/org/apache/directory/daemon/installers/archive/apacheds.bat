REM Licensed to the Apache Software Foundation (ASF) under one
REM or more contributor license agreements.  See the NOTICE file
REM distributed with this work for additional information
REM regarding copyright ownership.  The ASF licenses this file
REM to you under the Apache License, Version 2.0 (the
REM "License"); you may not use this file except in compliance
REM with the License.  You may obtain a copy of the License at
REM 
REM   http://www.apache.org/licenses/LICENSE-2.0
REM 
REM Unless required by applicable law or agreed to in writing,
REM software distributed under the License is distributed on an
REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
REM KIND, either express or implied.  See the License for the
REM specific language governing permissions and limitations
REM under the License.

@echo off

java -Dlog4j.configuration="file:conf/log4j.properties" -cp lib\antlr-2.7.7.jar;lib\apacheds-bootstrap-extract-1.5.3-SNAPSHOT.jar;lib\apacheds-bootstrap-partition-1.5.3-SNAPSHOT.jar;lib\apacheds-btree-base-1.5.3-SNAPSHOT.jar;lib\apacheds-core-1.5.3-SNAPSHOT.jar;lib\apacheds-core-constants-1.5.3-SNAPSHOT.jar;lib\apacheds-core-entry-1.5.3-SNAPSHOT.jar;lib\apacheds-core-shared-1.5.3-SNAPSHOT.jar;lib\apacheds-jdbm-1.5.3-SNAPSHOT.jar;lib\apacheds-jdbm-store-1.5.3-SNAPSHOT.jar;lib\apacheds-kerberos-shared-1.5.3-SNAPSHOT.jar;lib\apacheds-noarch-installer-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-changepw-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-dhcp-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-dns-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-kerberos-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-ldap-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-ntp-1.5.3-SNAPSHOT.jar;lib\apacheds-protocol-shared-1.5.3-SNAPSHOT.jar;lib\apacheds-schema-bootstrap-1.5.3-SNAPSHOT.jar;lib\apacheds-schema-extras-1.5.3-SNAPSHOT.jar;lib\apacheds-schema-registries-1.5.3-SNAPSHOT.jar;lib\apacheds-server-jndi-1.5.3-SNAPSHOT.jar;lib\apacheds-server-xml-1.5.3-SNAPSHOT.jar;lib\apacheds-utils-1.5.3-SNAPSHOT.jar;lib\apacheds-xbean-spring-1.5.3-SNAPSHOT.jar;lib\bootstrapper.jar;lib\commons-cli-1.1.jar;lib\commons-collections-3.2.jar;lib\commons-daemon-1.0.1.jar;lib\commons-lang-2.3.jar;lib\jcl104-over-slf4j-1.4.3.jar;lib\log4j-1.2.14.jar;lib\mina-core-1.1.6.jar;lib\mina-filter-ssl-1.1.6.jar;lib\shared-asn1-0.9.11-SNAPSHOT.jar;lib\shared-asn1-codec-0.9.11-SNAPSHOT.jar;lib\shared-bouncycastle-reduced-0.9.11-SNAPSHOT.jar;lib\shared-ldap-0.9.11-SNAPSHOT.jar;lib\shared-ldap-constants-0.9.11-SNAPSHOT.jar;lib\slf4j-api-1.4.3.jar;lib\slf4j-log4j12-1.4.3.jar;lib\spring-beans-2.0.6.jar;lib\spring-context-2.0.6.jar;lib\spring-core-2.0.6.jar;lib\xbean-spring-3.3.jar org.apache.directory.server.UberjarMain conf\server.xml