package com.workbench.authsyn.jinxin.service;

import com.workbench.auth.menu.entity.Menu;
import com.workbench.authsyn.jinxin.entity.JinxinOrganization;
import com.workbench.authsyn.jinxin.entity.JinxinUser;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;

public interface JinxinAuthSynService {

    List<JinxinUser> getUsersListFromJinxinUserCenter() throws IOException, URISyntaxException;
    List<JinxinOrganization> getOriginFromJinxinUserCenter();

    void trancatUsers();

    void saveUser(JinxinUser jinxinUser);

    void trancatOrigins();

    void saveOrigins(JinxinOrganization jinxinOrganization);

    void trancatUserOrigins();

    void saveUserOrigin(BigInteger userId, BigInteger organizationId);

    List<Menu> getUserAuths(BigInteger user_id) throws IOException, URISyntaxException;
}
