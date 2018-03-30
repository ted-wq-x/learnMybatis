package org.apache.ibatis.session;

import java.util.List;

/**
 * 项目名称：  mybatis-3<br>
 * 类名称：  User<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2018/3/28 0028 16:31
 */
public interface User {
    int queryUserIdByName(String name);

    List<Integer> queryAllId();

    UserBo queryUserById(int id);
}
