package com.shrey.kc.kcui.adaptors;

import com.shrey.kc.kcui.algos.graph.TagGraphM;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.blox.graphview.Graph;

public class OfflineDBAccessor {
    public static boolean addNewKnowledge(KCWriteRequest request) {
        ApplicationLocalDB localDB = LocalDBHolder.INSTANCE.getLocalDB();

        Tag tag = new Tag();
        tag.setTag(request.getKeyword());

        Knowledge knowledge = new Knowledge();
        knowledge.setKnowledge(request.getValue());

        // check if tag is already there, if yes, just link to that's id
        long[] tagIds = localDB.tagDao().findTagIds(new String[]{request.getKeyword()});
        if(tagIds == null || tagIds.length == 0) {
            tagIds = localDB.tagDao().insertAll(new Tag[]{tag});
        }

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

    public static NodeResult getAllTagsGraph(KCAccessRequest readRequest) {
        ApplicationLocalDB localDB = LocalDBHolder.INSTANCE.getLocalDB();
        String[] tags = localDB.tagDao().findTags();
        long[] tagIds = localDB.tagDao().findTagIds(tags);
        ArrayList<Integer> tagIdsAll = new ArrayList<>();
        Map<String, ArrayList<String>> relatedTagsMap = new HashMap<>();
        for(long tagId: tagIds) {
            long[] knowledgeIds = localDB.knowledgeTagMappingDao().findKnowledgesForTag(new long[]{tagId});
            long[] dependentTagIds = localDB.knowledgeTagMappingDao().findTagsForKnowledge(knowledgeIds);
            String[] dependentTags = localDB.tagDao().findTagsForIds(dependentTagIds);
            String tagString = localDB.tagDao().findTagsForIds(new long[]{tagId})[0];
            relatedTagsMap.put(tagString, new ArrayList<String>());
            for(String dt: dependentTags) {
                if(!dt.equals(tagString)) {
                    relatedTagsMap.get(tagString).add(dt);
                }
            }
        }
        TagGraphM graph = new TagGraphM();
        for(Map.Entry<String, ArrayList<String>> relationEntry: relatedTagsMap.entrySet()) {
            graph.addNode(relationEntry.getKey(), relationEntry.getValue());
        }
        NodeResult result = new NodeResult();
        HashMap<String, Object> resultGraph = new HashMap<>();
        resultGraph.put("Graph",graph);
        result.setResult(resultGraph);
        return result;
    }
}
