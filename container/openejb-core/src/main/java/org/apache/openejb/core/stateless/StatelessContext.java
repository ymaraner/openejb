/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.core.stateless;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.openejb.core.BaseSessionContext;
import org.apache.openejb.core.Operation;
import org.apache.openejb.spi.SecurityService;


/**
 * @version $Rev$ $Date$
 */
public class StatelessContext extends BaseSessionContext {

    public StatelessContext(TransactionManager transactionManager, SecurityService securityService) {
        super(transactionManager, securityService);
    }

    public StatelessContext(TransactionManager transactionManager, SecurityService securityService, UserTransaction userTransaction) {
        super(transactionManager, securityService, userTransaction);
    }

    /**
     * Business method from web service endpoint
     */
    private static class BusinessWsStatelessState extends StatelessState {
        public Class getInvokedBusinessInterface() {
            throw new IllegalStateException();
        }
    }

    static {
        states[Operation.INJECTION.ordinal()] = new InjectionStatelessState();
        states[Operation.LIFECYCLE.ordinal()] = new LifecycleStatelessState();
        states[Operation.BUSINESS.ordinal()] = new BusinessStatelessState();
        states[Operation.BUSINESS_WS.ordinal()] = new BusinessWsStatelessState();
        states[Operation.TIMEOUT.ordinal()] = new TimeoutStatelessState();
    }

}
