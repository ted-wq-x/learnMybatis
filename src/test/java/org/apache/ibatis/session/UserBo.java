package org.apache.ibatis.session;

/**
 * 项目名称：  mybatis-3<br>
 * 类名称：  UserBo<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2018/3/30 0030 16:05
 */
public class UserBo {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserBo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
