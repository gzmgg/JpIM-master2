package com.wapchief.jpushim.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.dell.jpim.ChatLog;
import com.example.dell.jpim.RequestList;
import com.example.dell.jpim.SearchAdd;
import com.example.dell.jpim.User;

import com.wapchief.jpushim.greendao.ChatLogDao;
import com.wapchief.jpushim.greendao.RequestListDao;
import com.wapchief.jpushim.greendao.SearchAddDao;
import com.wapchief.jpushim.greendao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatLogDaoConfig;
    private final DaoConfig requestListDaoConfig;
    private final DaoConfig searchAddDaoConfig;
    private final DaoConfig userDaoConfig;

    private final ChatLogDao chatLogDao;
    private final RequestListDao requestListDao;
    private final SearchAddDao searchAddDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatLogDaoConfig = daoConfigMap.get(ChatLogDao.class).clone();
        chatLogDaoConfig.initIdentityScope(type);

        requestListDaoConfig = daoConfigMap.get(RequestListDao.class).clone();
        requestListDaoConfig.initIdentityScope(type);

        searchAddDaoConfig = daoConfigMap.get(SearchAddDao.class).clone();
        searchAddDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        chatLogDao = new ChatLogDao(chatLogDaoConfig, this);
        requestListDao = new RequestListDao(requestListDaoConfig, this);
        searchAddDao = new SearchAddDao(searchAddDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(ChatLog.class, chatLogDao);
        registerDao(RequestList.class, requestListDao);
        registerDao(SearchAdd.class, searchAddDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        chatLogDaoConfig.clearIdentityScope();
        requestListDaoConfig.clearIdentityScope();
        searchAddDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public ChatLogDao getChatLogDao() {
        return chatLogDao;
    }

    public RequestListDao getRequestListDao() {
        return requestListDao;
    }

    public SearchAddDao getSearchAddDao() {
        return searchAddDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
