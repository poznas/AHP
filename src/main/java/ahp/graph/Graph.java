package ahp.graph;

import ahp.models.AHP;
import ahp.Main;
import ahp.models.PairwiseComparison;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;

public class Graph {

    static Color mainColor = Color.grayRgb(0, 0.702);
    static double levelHeight = 100.0;
    static double levelSpacing = 30.0;
    static Pane overlay;

    public static List<AHPNode[]> nodes;

    public static void draw() {
        if( overlay == null ){ overlay = Main.rootLayoutController.overlay; }
        overlay.getChildren().clear();
        Main.rootLayout.setCenter(overlay);
        nodes = getNodes();

        for( int level=0; level<nodes.size(); level++ ){ //level goes from head:0 to alternatives
            double leftMargin = 0.0;
            for( int i=0; i<nodes.get(level).length; i++ ){
                 leftMargin = nodes.get(level)[i].drawNode(
                         overlay, levelHeight, levelSpacing,
                         leftMargin, level, nodes.get(level).length );
            }
        }
        for( int level=0; level<nodes.size(); level++ ){
            for( int i=0; i<nodes.get(level).length; i++ ){
                nodes.get(level)[i].drawConnections();
            }
        }
    }

    private static List<AHPNode[]> getNodes() {
        List<AHPNode[]> nodes = new ArrayList<>();

        //HEAD
        PairwiseComparison head = PairwiseComparison.get(Main.AHP.headId);
        nodes.add(new AHPNode[]{new AHPNode(head.id, null, head.alternatives)});

        //ALTERNATIVES
        AHPNode[] alternatives = new AHPNode[Main.AHP.alternatives.size()];
        List<String> alternativeParents = AHP.getAlternativeParents();
        for( int i=0; i<alternatives.length; i++ ){
            alternatives[i] = new AHPNode(Main.AHP.alternatives.get(i), alternativeParents, null);
        }
        nodes.add(alternatives);

        //CRITERIONS
        if( alternativeParents.contains(Main.AHP.headId) ){
            return nodes;
        }
        List<String> upperParents = alternativeParents;
        while (upperParents != null){
            AHPNode[] upperLevel = new AHPNode[upperParents.size()];
            for( int i=0; i<upperLevel.length; i++ ){
                PairwiseComparison criterion = PairwiseComparison.get(upperParents.get(i));
                upperLevel[i] = new AHPNode(criterion.id, criterion.parents, criterion.alternatives);
            }
            nodes.add(1,upperLevel);
            upperParents = PairwiseComparison.getAllParentsCriterion(upperParents);
        }
        return nodes;
    }
}
