package com.ruida.sharding.jdbc.learning.config;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DaoConfig {
    @Bean("ds0DataSource")
    @ConfigurationProperties(prefix = "spring.datasource0")
    DataSource ds0DataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * ds1 (read only) data source.
     */
    @Bean("ds1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource1")
    DataSource ds1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    DataSource primaryDataSource(
        @Autowired @Qualifier("ds0DataSource") DataSource ds0DataSource,
        @Autowired @Qualifier("ds1DataSource") DataSource ds1DataSource
    ) throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0", ds0DataSource);
        dataSourceMap.put("ds1", ds1DataSource);


        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig =
            new TableRuleConfiguration("t_order", "ds${0..1}.t_order${0..1}");
        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "t_order${id % 2}"));
        //key生成算法
        orderTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "id"));
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
        return dataSource;
    }
}
