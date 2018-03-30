package org.apache.ibatis.session;

import org.apache.ibatis.io.Resources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

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


    @Test
    public void testV2(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("org.apache.ibatis.session.User.queryAllId");
        System.out.println(objects);
    }

    /**
     * 测试已经缓存，没有在select语句上添加属性flushCache=true
     * 没有限制一级缓存，所以两次执行查询，实际只执行了一次，同时发现第二次查询出的对象名字被修改了
     *
     */
    @Test
    public void testCache(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBo userBo = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo);
        Assert.assertEquals(userBo.getName(),"wq");
        userBo.setName("another");
        UserBo userBo1 = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo1);
        Assert.assertEquals(userBo1.getName(),"another");
    }


    /**
     * 第二次缓存测试，添加flushCache=true，针对单条sql，执行了两次sql，一级缓存失效
     */
    @Test
    public void testCacheWithAttrTrue(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBo userBo = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo);
        Assert.assertEquals(userBo.getName(),"wq");
        userBo.setName("another");
        UserBo userBo1 = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo1);
        Assert.assertEquals(userBo1.getName(),"wq");
    }


    /**
     * 第三次缓存测试，将一级缓存的级别修改成statement，在setting.xml中设置的
     */
    @Test
    public void testCacheWithAttrStatement(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBo userBo = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo);
        Assert.assertEquals(userBo.getName(),"wq");
        userBo.setName("another");
        UserBo userBo1 = sqlSession.selectOne("org.apache.ibatis.session.User.queryUserById", 1);

        System.out.println(userBo1);
        Assert.assertEquals(userBo1.getName(),"wq");
    }
}
