package ahp.models;

import ahp.Main;
import ahp.graph.Graph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AHP {

    public String headId = "";
    public List<PairwiseComparison> comparisons = new ArrayList<>();
    public List<String> alternatives = new ArrayList<>();

    public static boolean idOccupied( String id ){
        for( PairwiseComparison comparison : Main.AHP.comparisons ){
            if (comparison.id.equals(id)){
                return true;
            }
        }
        return Main.AHP.headId.equals(id);
    }

    public static void initComparisons(){
        if( Main.AHP.comparisons.size() > 0 ){
            return;
        }
        PairwiseComparison comparison = new PairwiseComparison();
        comparison.alternatives = Main.AHP.alternatives;
        comparison.id = Main.AHP.headId;
        Main.AHP.comparisons.add(comparison);
    }

    public static List<String> getAlternativeParents(){
        List<String> parents = new ArrayList<>();
        for( PairwiseComparison comparison : Main.AHP.comparisons ){
            if( areBasicAlternatives(comparison.alternatives, Main.AHP.alternatives) ){
                parents.add(comparison.id);
            }
        }
        return parents;
    }

    public static boolean areBasicAlternatives(List<String> alternatives, List<String> basic ) {
        return alternatives.contains(basic.get(0));
    }

    public static void saveToJson() {
        if (Main.currentFile == null) {
            return;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(Main.currentFile)) {
            gson.toJson(Main.AHP, AHP.class, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader(Main.currentFile));
            Main.AHP = new Gson().fromJson(reader, AHP.class);
            Graph.draw();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class Rebuilder {

        String oldHeadId;
        List<String> oldAlternatives;

        public Rebuilder(String headId, List<String> alternatives) {
            oldHeadId = headId;
            oldAlternatives = alternatives;
        }

        public void invoke() {
            if( oldAlternatives == null || oldAlternatives.size() == 0 ){ return; }
            if( oldAlternatives != Main.AHP.alternatives ){
                System.out.println("rebuilder alternatives");
                for( PairwiseComparison comparison : Main.AHP.comparisons ){
                    if( comparison.alternatives != null ){
                        if( AHP.areBasicAlternatives(comparison.alternatives, oldAlternatives)){
                            comparison.alternatives = Main.AHP.alternatives;
                        }
                    }
                }
            }
            if( !oldHeadId.equals(Main.AHP.headId) ){
                for( PairwiseComparison comparison : Main.AHP.comparisons ){
                    if( comparison.id.equals(oldHeadId) ){
                        comparison.id = Main.AHP.headId;
                    }
                    if( comparison.parents != null ){
                        for( int i=0; i<comparison.parents.size(); i++ ){
                            if( comparison.parents.get(i).equals(oldHeadId) ){
                                comparison.parents.set(i, Main.AHP.headId);
                            }
                        }
                    }
                    if( comparison.alternatives != null ){
                        for( int i=0; i<comparison.alternatives.size(); i++ ){
                            if( comparison.alternatives.get(i).equals(oldHeadId) ){
                                comparison.alternatives.set(i, Main.AHP.headId);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void print(){
        System.out.println(
                new GsonBuilder().setPrettyPrinting().create().toJson(Main.AHP)
        );
    }
}
