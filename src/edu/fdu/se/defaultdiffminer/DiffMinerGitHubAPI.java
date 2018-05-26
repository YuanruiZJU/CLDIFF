package edu.fdu.se.defaultdiffminer;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.astdiff.associating.FileInsideAssociationGenerator;
import edu.fdu.se.astdiff.associating.FileOutsideGenerator;
import edu.fdu.se.astdiff.associating.TotalFileAssociations;
import edu.fdu.se.astdiff.associating.similarity.TreeDistance;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;
import edu.fdu.se.main.astdiff.BaseDiffMiner;
import edu.fdu.se.main.astdiff.DiffMinerTest;
import edu.fdu.se.main.astdiff.RQ.JGitHelper;
import edu.fdu.se.main.astdiff.RQ.RQ;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ObservableInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by huangkaifeng on 2018/4/12.
 *
 *
 */
public class DiffMinerGitHubAPI {

    private Map<String,ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public BaseDiffMiner baseDiffMiner;
    public String outputDir;
    private String commitId;
    private List<FilePairData> filePairDatas;

    public DiffMinerGitHubAPI(String path) {
        filePairDatas = new ArrayList<>();
        baseDiffMiner = new BaseDiffMiner();
        File file = new File(path+"/meta.json");
        String content = "";
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject=new JSONObject(content);
        String commit = jsonObject.getString("commitId");
        String projName = jsonObject.getString("projName");
        baseDiffMiner.mFileOutputLog = new FileOutputLog(path, projName);
        baseDiffMiner.mFileOutputLog.setCommitId(commit);
        this.commitId = commit;
        this.outputDir = path;
        initData(jsonObject);
        this.generateDiffMinerOutput();
    }

    public void initData(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("files");
        Iterator iter = jsonArray.iterator();
        while(iter.hasNext()){
            JSONObject jo = (JSONObject) iter.next();
            String fileFullName = jo.getString("fileName");
            int index = fileFullName.lastIndexOf("/");
            String fileName = fileFullName.substring(index + 1, fileFullName.length());
            String prevFilePath = jo.getString("prevFilePath");
            String currFilePath = jo.getString("currFilePath");
            FilePairData fp = new FilePairData(null,null,prevFilePath,currFilePath,fileName);
            filePairDatas.add(fp);
//            this.baseDiffMiner.mFileOutputLog.writeSourceFile(prevFile,currFile,fileName);
        }
    }


    public void generateDiffMinerOutput(){
        String absolutePath = this.outputDir+"\\" + this.commitId;
        for(FilePairData fp:filePairDatas){
            System.out.println(fp.fileName);
            Global.fileName = fp.fileName;
            if(fp.prev==null){
                this.baseDiffMiner.dooNewFile(fp.fileName,fp.curr,absolutePath);
            }else {
                this.baseDiffMiner.doo(fp.fileName, fp.prev, fp.curr, absolutePath);
            }
            this.fileChangeEntityData.put(this.baseDiffMiner.changeEntityData.fileName,this.baseDiffMiner.changeEntityData);
        }
//        if(true) return;
        List<String> fileNames = new ArrayList<>(this.fileChangeEntityData.keySet());
        TotalFileAssociations totalFileAssociations = new TotalFileAssociations() ;
        for(int i =0;i<fileNames.size();i++){
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            FileInsideAssociationGenerator associationGenerator = new FileInsideAssociationGenerator(cedA);
            associationGenerator.generateFile();
            totalFileAssociations.addEntry(fileNameA,cedA.mAssociations);
        }
        for(int i =0;i<fileNames.size();i++){
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            FileOutsideGenerator fileOutsideGenerator = new FileOutsideGenerator();
            for(int j =i+1;j<fileNames.size();j++){
                String fileNameB = fileNames.get(j);
                ChangeEntityData cedB = this.fileChangeEntityData.get(fileNameB);
                fileOutsideGenerator.generateOutsideAssociation(cedA,cedB);
                totalFileAssociations.addFile2FileAssos(fileNameA,fileNameB,fileOutsideGenerator.mAssos);
            }
        }
        new FileOutsideGenerator().checkDuplicateSimilarity(this.fileChangeEntityData);
        baseDiffMiner.mFileOutputLog.writeLinkJson(totalFileAssociations.toAssoJSonString());
//        System.out.println(totalFileAssociations.toAssoJSonString());
        System.out.println(totalFileAssociations.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1,Tree tree2){
        TreeDistance treeDistance = new TreeDistance(tree1,tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}