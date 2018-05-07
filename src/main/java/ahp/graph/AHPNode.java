package ahp.graph;

import ahp.Main;
import ahp.models.AHP;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.util.List;
import java.util.Objects;

public class AHPNode extends HBox {

    String id;
    List<String> parents;
    List<String> alternatives;

    double[] center;
    Canvas canvas;
    private static final double textSize = 2.0;
    private static final double lineWidth = 4.0;
    private static final double alternativeLineWidth = 2.0;

    public void print(){
        if( parents != null){
            if( alternatives != null ){
                System.out.println(id + " | P:"+parents.toString()+" | A:"+alternatives.toString());
            }else {
                System.out.println(id + " | P:"+parents.toString());
            }
        }else{
            System.out.println(id + " | A:"+alternatives.toString());
        }

    }


    public AHPNode(String id, List<String> parents, List<String> alternatives) {
        this.id = id;
        this.parents = parents;
        this.alternatives = alternatives;
        setStyle("-fx-padding: 10;" + "-fx-border-style: solid;"
                + "-fx-border-width: 2;" + "-fx-border-color: blue;"
                + "-fx-background-color: white");
        canvas = getCanvasLabel(0,0, id);
        getChildren().add(canvas);
        setWidth(canvas.getWidth() + 20.0 + 14.0 );
        setHeight(canvas.getHeight());
        print();
    }

    public static Canvas getCanvasLabel(double targetX, double targetY, String latex){
        Canvas latexCanvas;
        latexCanvas = getLatexCanvas(latex,textSize);
        latexCanvas.relocate(targetX, targetY);
        return latexCanvas;
    }

    private static Canvas getLatexCanvas(String latex, double size ){
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
        Canvas latexCanvas = new Canvas(icon.getIconWidth(),icon.getIconHeight());
        icon.paintInCanvas(latexCanvas,0,0, Graph.mainColor, Color.TRANSPARENT);
        return latexCanvas;
    }

    public double drawNode(Pane overlay, double levelHeight, double levelSpacing, double leftMargin, int level, int levelLength) {
        overlay.getChildren().add(this);
        double width = getWidth();
        double height = getHeight();
        double targetX, targetY;
        if( leftMargin == 0.0 ){
            leftMargin = overlay.getWidth()/(2*levelLength) - width/2;
        }
        targetY = levelHeight * ( level + 0.5 ) - height/2;
        targetX = leftMargin + levelSpacing;
        relocate(targetX, targetY);

        center = new double[]{targetX + width/2, targetY + height/2};
        return targetX + width;
    }

    public void drawConnections() {
        if( alternatives == null ){ return; }
        boolean thinLine = AHP.areBasicAlternatives(alternatives, Main.AHP.alternatives);
        for( String aid : alternatives ){
            double[] alternativeCenter = Objects.requireNonNull(get(aid)).center;
            Line line = new Line(alternativeCenter[0], alternativeCenter[1], center[0], center[1]);
            line.setStroke(Graph.mainColor);
            if( thinLine ){
                line.setStrokeWidth(alternativeLineWidth);
            }else{
                line.setStrokeWidth(lineWidth);
            }
            Graph.overlay.getChildren().add(0, line);
        }
    }

    private static AHPNode get(String aid) {
        for( int level=0; level<Graph.nodes.size(); level++ ){ //level goes from head:0 to alternatives
            for( int i=0; i<Graph.nodes.get(level).length; i++ ){
                if( Graph.nodes.get(level)[i].id.equals(aid)){
                    return Graph.nodes.get(level)[i];
                }
            }
        }
        return null;
    }
}