package com.shrey.kc.kcui.adaptors;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.localdb.Knowledge;
import com.shrey.kc.kcui.localdb.KnowledgeTagMapping;
import com.shrey.kc.kcui.localdb.Tag;
import com.shrey.kc.kcui.objects.ApplicationLocalDB;
import com.shrey.kc.kcui.objects.LocalDBHolder;

import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OfflineDBAccessor {
    public static boolean addNewKnowledge(KCWriteRequest request) {
        ApplicationLocalDB localDB = LocalDBHolder.INSTANCE.getLocalDB();

        Tag tag = new Tag();
        tag.setTag(request.getKeyword());

        Knowledge knowledge = new Knowledge();
        knowledge.setKnowledge(request.getValue());

        // write these
        long[] tagIds = localDB.tagDao().insertAll(new Tag[]{tag});

        long[] knowledgeIds = localDB.knowledgeDao().insertAll(new Knowledge[]{knowledge});

        // persist mapping
        KnowledgeTagMapping knowledgeTagMapping = new KnowledgeTagMapping();
        knowledgeTagMapping.setTagId(tagIds[0]);
        knowledgeTagMapping.setKnowledgeId(knowledgeIds[0]);

        localDB.knowledgeTagMappingDao().insertAll(knowledgeTagMapping);
        return true;
    }

    public static NodeResult getKnowledge(KCReadRequest readRequest) {
        ApplicationLocalDB localDB = LocalDBHolder.INSTANCE.getLocalDB();
        String[] tags = readRequest.getKeywordList().toArray(new String[]{});
        long[] tagIds = localDB.tagDao().findTagIds(tags);
        long[] knowledgeIds = localDB.knowledgeTagMappingDao().findKnowledgesForTag(tagIds);

        String[] knowledges = localDB.knowledgeDao().getById(knowledgeIds);

        NodeResult result = new NodeResult();
        result.setResult(new HashMap<String, Object>());

        result.getResult().put("Knowledge", new ArrayList<Object>());

        for(String knowledge: knowledges) {

            LinkedHashMap<String, String> resultInside = new LinkedHashMap<>();
            resultInside.put("cloud", knowledge);
            ((ArrayList<Object>)result.getResult().get("Knowledge")).add(resultInside);
        }
        return result;
    }

    public static NodeResult getAllTags(KCAccessRequest readRequest) {
        ApplicationLocalDB localDB = LocalDBHolder.INSTANCE.getLocalDB();
        String[] tags = localDB.tagDao().findTags();
        NodeResult result = new NodeResult();
        result.setResult(new HashMap<String, Object>());
        result.getResult().put("Tags", tags);
        return result;
    }
}
