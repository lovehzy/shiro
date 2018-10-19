package com.hzy.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzy.dao.IPermissionDAO;
import com.hzy.dao.IRoleDAO;
import com.hzy.dao.IUserDAO;
import com.hzy.pojo.User;

public class MyRealm extends AuthorizingRealm{
	 	@Autowired
	    private IUserDAO userDAO;
	    @Autowired
	    private IRoleDAO roleDAO;
	    @Autowired
	    private IPermissionDAO permissionDAO;

	    //认证操作
	    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	        //从token中获取登录的用户名， 查询数据库返回用户信息
	        String username = (String) token.getPrincipal();
	        User user = userDAO.getUserByUsername(username);

	        if(user == null){
	            return null;
	        }
	        //参数一:凭证信息，参数二：从数据查询出来的密码，参数三:盐值，参数四：自定义的realm名称
	        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
	                ByteSource.Util.bytes(user.getUsername()),
	                getName());
	        return info;
	    }



	    @Override
	    public String getName() {
	        return "MyRealm";
	    }

	    //授权操作
	    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

	        User user = (User) principals.getPrimaryPrincipal();   //获取需要授权的凭证
	        List<String> permissions = new ArrayList<String>();    //存放权限的容器
	        List<String> roles = new ArrayList<>();                //存放角色的容器
	        //判断该登陆的用户是否为管理员，如果是就授予所有的权限
	        if("admin".equals(user.getUsername())){
	            //拥有所有权限
	            permissions.add("*:*");
	            //查询所有角色
	            roles = roleDAO.getAllRoleSn();
	        }else{
	            //根据用户id查询该用户所具有的角色
	            roles = roleDAO.getRoleSnByUserId(user.getId());
	            //根据用户id查询该用户所具有的权限
	            permissions = permissionDAO.getPermissionResourceByUserId(user.getId());
	        }
	        //将角色信息，权限信息添加到SimpleAuthorizationInfo，并返回
	        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	        info.addStringPermissions(permissions);
	        info.addRoles(roles);
	        return info;
	    }

}
