package org.apache.ibatis.session;

import org.apache.ibatis.io.Resources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * 项目名称：  mybatis-3<br>
 * 类名称：  MyTest<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2018/3/28 0028 16:43
 */
public class MyTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void init() throws IOException {

        Reader resourceAsReader = Resources.getResourceAsReader("org/apache/ibatis/session/mySetting.xml");
        sqlSessionFactory= new SqlSessionFactoryBuilder().build(resourceAsReader);

    }


    @Test
    public void testV1(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object wq = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserIdByName", "wq");
        Assert.assertEquals(1,wq);
    }
}
