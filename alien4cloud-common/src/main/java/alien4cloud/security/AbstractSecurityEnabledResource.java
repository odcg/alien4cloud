package alien4cloud.security;

import static alien4cloud.dao.model.FetchContext.SUMMARY;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.annotation.NestedObject;
import org.elasticsearch.annotation.query.FetchContext;
import org.elasticsearch.annotation.query.TermFilter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import alien4cloud.security.ISecurityEnabledResource;
import alien4cloud.security.Permission;
import alien4cloud.security.Subject;
import alien4cloud.utils.jackson.ConditionalAttributes;
import alien4cloud.utils.jackson.ConditionalOnAttribute;
import alien4cloud.utils.jackson.JSonMapEntryArrayDeSerializer;
import alien4cloud.utils.jackson.JSonMapEntryArraySerializer;
import alien4cloud.utils.jackson.NotAnalyzedTextMapEntry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractSecurityEnabledResource implements ISecurityEnabledResource {

    @TermFilter(paths = { "key", "value" })
    @NestedObject(nestedClass = NotAnalyzedTextMapEntry.class)
    @ConditionalOnAttribute(ConditionalAttributes.ES)
    @JsonDeserialize(using = JSonMapEntryArrayDeSerializer.class)
    @JsonSerialize(using = JSonMapEntryArraySerializer.class)
    @FetchContext(contexts = { SUMMARY }, include = { true })
    private Map<String, Set<Permission>> userPermissions;

    @TermFilter(paths = { "key", "value" })
    @NestedObject(nestedClass = NotAnalyzedTextMapEntry.class)
    @ConditionalOnAttribute(ConditionalAttributes.ES)
    @JsonDeserialize(using = JSonMapEntryArrayDeSerializer.class)
    @JsonSerialize(using = JSonMapEntryArraySerializer.class)
    @FetchContext(contexts = { SUMMARY }, include = { true })
    private Map<String, Set<Permission>> groupPermissions;

    @TermFilter(paths = { "key", "value" })
    @NestedObject(nestedClass = NotAnalyzedTextMapEntry.class)
    @ConditionalOnAttribute(ConditionalAttributes.ES)
    @JsonDeserialize(using = JSonMapEntryArrayDeSerializer.class)
    @JsonSerialize(using = JSonMapEntryArraySerializer.class)
    @FetchContext(contexts = { SUMMARY }, include = { true })
    private Map<String, Set<Permission>> applicationPermissions;

    @TermFilter(paths = { "key", "value" })
    @NestedObject(nestedClass = NotAnalyzedTextMapEntry.class)
    @ConditionalOnAttribute(ConditionalAttributes.ES)
    @JsonDeserialize(using = JSonMapEntryArrayDeSerializer.class)
    @JsonSerialize(using = JSonMapEntryArraySerializer.class)
    @FetchContext(contexts = { SUMMARY }, include = { true })
    private Map<String, Set<Permission>> environmentPermissions;

    private Map<String, Set<Permission>> getPermissions(Subject subjectType) {
        switch (subjectType) {
        case USER:
            return userPermissions == null ? new HashMap<>() : getUserPermissions();
        case GROUP:
            return groupPermissions == null ? new HashMap<>() : getGroupPermissions();
        case APPLICATION:
            return applicationPermissions == null ? new HashMap<>() : getApplicationPermissions();
        case ENVIRONMENT:
            return environmentPermissions == null ? new HashMap<>() : getEnvironmentPermissions();
        default:
            return new HashMap<>();
        }
    }

    private void setPermissions(Subject subjectType, Map<String, Set<Permission>> permissions) {
        switch (subjectType) {
        case USER:
            setUserPermissions(permissions);
            break;
        case GROUP:
            setGroupPermissions(permissions);
            break;
        case APPLICATION:
            setApplicationPermissions(permissions);
            break;
        case ENVIRONMENT:
            setEnvironmentPermissions(permissions);
            break;
        }
    }

    @Override
    public Set<Permission> getPermissions(Subject subjectType, String subject) {
        Set<Permission> permissions = getPermissions(subjectType).get(subject);
        return permissions == null ? new HashSet<>() : permissions;
    }

    @Override
    public void addPermissions(Subject subjectType, String subject, Set<Permission> permissions) {
        Map<String, Set<Permission>> allPermissions = getPermissions(subjectType);
        Set<Permission> newPermissions = allPermissions.get(subject);
        if (newPermissions == null) {
            newPermissions = new HashSet<>();
        }
        newPermissions.addAll(permissions);
        allPermissions.put(subject, newPermissions);
        setPermissions(subjectType, allPermissions);
    }

    @Override
    public void removePermissions(Subject subjectType, String subject, Set<Permission> permissions) {
        Map<String, Set<Permission>> allPermissions = getPermissions(subjectType);
        if (allPermissions.isEmpty()) {
            return;
        }
        Set<Permission> newPermissions = allPermissions.get(subject);
        if (newPermissions == null) {
            allPermissions.remove(subject);
        } else {
            newPermissions.removeAll(permissions);
            if (newPermissions.isEmpty()) {
                allPermissions.remove(subject);
            } else {
                allPermissions.put(subject, newPermissions);
            }
        }
        if (allPermissions.isEmpty()) {
            allPermissions = null;
        }
        setPermissions(subjectType, allPermissions);
    }
}