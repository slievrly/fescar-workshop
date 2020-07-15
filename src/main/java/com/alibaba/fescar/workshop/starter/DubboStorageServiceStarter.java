/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alibaba.fescar.workshop.starter;

import com.alibaba.fescar.workshop.ApplicationKeeper;
import com.alibaba.fescar.workshop.DBType;
import com.alibaba.fescar.workshop.EnvContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The type Dubbo storage service starter.
 *
 * @author jimin.jm @alibaba-inc.com
 */
public class DubboStorageServiceStarter {
    /**
     * 1. Storage service is ready . A seller add 100 storage to a sku: C00321
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext storageContext = new ClassPathXmlApplicationContext(
            new String[] {"spring/dubbo-storage-service.xml"});
        storageContext.getBean("service");
        JdbcTemplate storageJdbcTemplate = (JdbcTemplate)storageContext.getBean("jdbcTemplate");
        storageJdbcTemplate.update("delete from storage_tbl where commodity_code = 'C00321'");
        if (EnvContext.dbType == DBType.MYSQL || EnvContext.dbType == DBType.PGSQL) {
            storageJdbcTemplate.update("insert into storage_tbl(commodity_code, count) values ('C00321', 100)");
        } else if (EnvContext.dbType == DBType.ORACLE) {
            storageJdbcTemplate.update(
                "insert into storage_tbl(id,commodity_code, count) values (STORAGE_TBL_SEQ.nextval,'C00321', 100)");
        }
        new ApplicationKeeper(storageContext).keep();
    }
}
