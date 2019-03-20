/*
 * Copyright 2016 Shunyi Chen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户/员工
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_USER")
public class User implements Serializable {

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    @Column(name = "USER_NAME", insertable = true, updatable = true, nullable = true)
    private String userName;
    
    @Column(name = "PASSWORD", insertable = true, updatable = true, nullable = true)
    private String password;
    
    @Column(name = "DISPLAY_NAME", insertable = true, updatable = true, nullable = true)
    private String displayName;

    @Column(name = "CREATED_TIME", insertable = true, updatable = true, nullable = true)
    private Date createTime;
    
    @Column(name = "CREATOR_ID", insertable = true, updatable = true, nullable = true)
    private String creatorId;
    
    @Column(name = "UPDATED_TIME", insertable = true, updatable = true, nullable = true)
    private Date updateTime;
    
    @Column(name = "UPDATOR_ID", insertable = true, updatable = true, nullable = true)
    private String updatorId;
    
    @Column(name = "DELETED_TIME", insertable = true, updatable = true, nullable = true)
    private Date deleteTime;
    
    @Column(name = "DELETOR_ID", insertable = true, updatable = true, nullable = true)
    private String deletorId;
    
    @Column(name = "UPDATED_COUNT", insertable = true, updatable = true, nullable = true)
    private int updatedCount;
    
    @Column(name = "DELETE_FLAG", insertable = true, updatable = true, nullable = true)
    private int deleteFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeletorId() {
        return deletorId;
    }

    public void setDeletorId(String deletorId) {
        this.deletorId = deletorId;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    public static User create(String userName, String password, String displayName) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setDisplayName(displayName);
        user.setCreatorId("System");
        user.setCreateTime(new Date());
        return user;
    }
}
