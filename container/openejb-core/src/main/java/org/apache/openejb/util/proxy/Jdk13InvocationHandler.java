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
package org.apache.openejb.util.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Jdk13InvocationHandler implements java.lang.reflect.InvocationHandler, Serializable {
    private org.apache.openejb.util.proxy.InvocationHandler delegate;

    public Jdk13InvocationHandler() {
    }

    public Jdk13InvocationHandler(org.apache.openejb.util.proxy.InvocationHandler delegate) {
        setInvocationHandler(delegate);
    }

    public org.apache.openejb.util.proxy.InvocationHandler getInvocationHandler() {
        return delegate;
    }

    public org.apache.openejb.util.proxy.InvocationHandler setInvocationHandler(org.apache.openejb.util.proxy.InvocationHandler handler) {
        org.apache.openejb.util.proxy.InvocationHandler old = delegate;
        delegate = handler;
        return old;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (delegate != null) {
            if (args == null) {
                args = new Object[0];

            }
            return delegate.invoke(proxy, method, args);
        } else {
            throw new NullPointerException("No invocation handler for proxy " + proxy);
        }
    }
}

