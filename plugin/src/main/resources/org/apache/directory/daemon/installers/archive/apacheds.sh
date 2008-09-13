#!/bin/sh
#
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing,
#  software distributed under the License is distributed on an
#  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#  KIND, either express or implied.  See the License for the
#  specific language governing permissions and limitations
#  under the License.
#
java -Dlog4j.configuration=file:conf/log4j.properties -cp lib/antlr-2.7.7.jar:lib/apacheds-bootstrap-extract-1.5.5-SNAPSHOT.jar:lib/apacheds-bootstrap-partition-1.5.5-SNAPSHOT.jar:lib/apacheds-btree-base-1.5.5-SNAPSHOT.jar:lib/apacheds-core-1.5.5-SNAPSHOT.jar:lib/apacheds-core-avl-1.5.5-SNAPSHOT.jar:lib/apacheds-core-constants-1.5.5-SNAPSHOT.jar:lib/apacheds-core-cursor-1.5.5-SNAPSHOT.jar:lib/apacheds-core-entry-1.5.5-SNAPSHOT.jar:lib/apacheds-core-jndi-1.5.5-SNAPSHOT.jar:lib/apacheds-core-shared-1.5.5-SNAPSHOT.jar:lib/apacheds-jdbm-1.5.5-SNAPSHOT.jar:lib/apacheds-jdbm-store-1.5.5-SNAPSHOT.jar:lib/apacheds-kerberos-shared-1.5.5-SNAPSHOT.jar:lib/apacheds-noarch-installer-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-changepw-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-dhcp-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-dns-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-kerberos-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-ldap-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-ntp-1.5.5-SNAPSHOT.jar:lib/apacheds-protocol-shared-1.5.5-SNAPSHOT.jar:lib/apacheds-schema-bootstrap-1.5.5-SNAPSHOT.jar:lib/apacheds-schema-extras-1.5.5-SNAPSHOT.jar:lib/apacheds-schema-registries-1.5.5-SNAPSHOT.jar:lib/apacheds-server-jndi-1.5.5-SNAPSHOT.jar:lib/apacheds-server-xml-1.5.5-SNAPSHOT.jar:lib/apacheds-utils-1.5.5-SNAPSHOT.jar:lib/apacheds-xbean-spring-1.5.5-SNAPSHOT.jar:lib/apacheds-xdbm-search-1.5.5-SNAPSHOT.jar:lib/apacheds-xdbm-tools-1.5.5-SNAPSHOT.jar:lib/bootstrapper.jar:lib/commons-cli-1.1.jar:lib/commons-collections-3.2.1.jar:lib/commons-daemon-1.0.1.jar:lib/commons-io-1.4.jar:lib/commons-lang-2.4.jar:lib/jcl-over-slf4j-1.5.2.jar:lib/log4j-1.2.14.jar:lib/mina-core-1.1.7.jar:lib/mina-filter-ssl-1.1.7.jar:lib/shared-asn1-0.9.13-SNAPSHOT.jar:lib/shared-asn1-codec-0.9.13-SNAPSHOT.jar:lib/shared-bouncycastle-reduced-0.9.13-SNAPSHOT.jar:lib/shared-ldap-0.9.13-SNAPSHOT.jar:lib/shared-ldap-constants-0.9.13-SNAPSHOT.jar:lib/slf4j-api-1.5.2.jar:lib/slf4j-log4j12-1.5.2.jar:lib/spring-beans-2.5.5.jar:lib/spring-context-2.5.5.jar:lib/spring-core-2.5.5.jar:lib/xbean-spring-3.4.3.jar org.apache.directory.server.UberjarMain conf/server.xml
