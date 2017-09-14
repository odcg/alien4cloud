package alien4cloud.variable;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;

public class YamlToProperties {

    public static Properties from(Resource resource){
        YamlFullPropertiesFactoryBean factoryBean = new YamlFullPropertiesFactoryBean();
        factoryBean.setResources(resource);
        return factoryBean.getObject();
    }

    public static class YamlFullPropertiesFactoryBean extends YamlPropertiesFactoryBean {

        @Override
        protected Properties createProperties() {
            final Properties result = new Properties();
            process((properties, map) -> {
                result.putAll(properties);
                processMap("", result, map);
            });
            return result;
        }

        private void processMap(String parentKey, Properties result, Map<String, Object> map) {
            map.forEach((k, v) -> {
                result.put(parentKey + k, v);

                if (Map.class.isAssignableFrom(v.getClass())) {
                    processMap(parentKey + k + ".", result, (Map<String, Object>) v);
                }
            });
        }
    }

}
