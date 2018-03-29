/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PluginTest {

  @Test
  public void myTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
    final String resource = "org/apache/ibatis/builder/MapperConfig.xml";
    final InterceptorChain interceptorChain = new InterceptorChain();
    Reader resourceAsStream = Resources.getResourceAsReader(resource);
    XPathParser xPathParser = new XPathParser(resourceAsStream);
    XNode xNode = xPathParser.evalNode("/configuration").evalNode("plugins");
    if (xNode != null) {
      for (XNode child : xNode.getChildren()) {
        String interceptor = child.getStringAttribute("interceptor");
        Properties properties = child.getChildrenAsProperties();
        Interceptor interceptorInstance = (Interceptor) PluginTest.class.getClassLoader().loadClass(interceptor)
                .newInstance();
        interceptorInstance.setProperties(properties);
        interceptorChain.addInterceptor(interceptorInstance);
      }
    }

    Map map = new HashMap();
    map.put("name", "wq");
    // jdk动态代理
    map = (Map) interceptorChain.pluginAll(map);

    System.out.println(map.get("name"));
    System.out.println(map.get("age"));
  }

  @Test
  public void mapPluginShouldInterceptGet() {
    Map map = new HashMap();
    map = (Map) new AlwaysMapPlugin().plugin(map);
    assertEquals("Always", map.get("Anything"));
  }

  @Test
  public void shouldNotInterceptToString() {
    Map map = new HashMap();
    map = (Map) new AlwaysMapPlugin().plugin(map);
    assertFalse("Always".equals(map.toString()));
  }



}
/**
 * 拦截的是map接口的get方法
 */
@Intercepts({
        @Signature(type = Map.class, method = "get", args = {Object.class})})
 class AlwaysMapPlugin implements Interceptor {

  private Properties properties;

  /**
   * 返回值总是always
   * @param invocation
   * @return
   * @throws Throwable
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {

    return "Always:"+invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    if (target instanceof Map) {
      Map map = (Map) target;
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        map.put(entry.getKey(), entry.getValue());
      }
      return Plugin.wrap(map, this);
    }
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    this.properties = properties;

  }
}