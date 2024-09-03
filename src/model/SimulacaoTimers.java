package model;

import javafx.animation.AnimationTimer;
import model.Corpo;
import model.CorposController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulacaoTimers {
    private List<Corpo> corpos;
    private CorposController corposController;

    public SimulacaoTimers(List<Corpo> corpos) {
        this.corpos = corpos;
        this.corposController = new CorposController();
    }

    public AnimationTimer createGravidadeTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<Corpo> corpos2 = new ArrayList<>(corpos);
    
                Iterator<Corpo> iterator = corpos2.iterator();
                while (iterator.hasNext()) {
                    Corpo c = iterator.next();
                    iterator.remove();
                    for (Corpo d : corpos2) {
                        corposController.atualizarPosicao(c, d);
                        System.out.println("Distancia entre " + c.getNome() + " e " + d.getNome() + Distancia(c, d));
                    }
                }
            }
        };
    }

    public AnimationTimer createColisaoTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<Corpo> corpos2 = new ArrayList<>(corpos);
    
                Iterator<Corpo> iterator = corpos2.iterator();
                while (iterator.hasNext()) {
                    Corpo c = iterator.next();
                    iterator.remove();
                    for (Corpo d : corpos2) {
                        if (colisao(c, d)) {
                            double v1_c = c.GetVelocidadeX() * c.getMassa() - d.GetVelocidadeX() * d.getMassa();
                            double v2_c = c.GetVelocidadeY() * c.getMassa() - d.GetVelocidadeY() * d.getMassa();
                            double v3_c = c.GetVelocidadeZ() * c.getMassa() - d.GetVelocidadeZ() * d.getMassa();
    
                            double v1_d = d.GetVelocidadeX() * d.getMassa() - c.GetVelocidadeX() * c.getMassa();
                            double v2_d = d.GetVelocidadeY() * d.getMassa() - c.GetVelocidadeY() * c.getMassa();
                            double v3_d = d.GetVelocidadeZ() * d.getMassa() - c.GetVelocidadeZ() * c.getMassa();
    
                            c.SetVelocidadeX(c.GetVelocidadeX() - (v1_c / c.getMassa()));
                            c.SetVelocidadeY(c.GetVelocidadeY() - (v2_c / c.getMassa()));
                            c.SetVelocidadeZ(c.GetVelocidadeZ() - (v3_c / c.getMassa()));
    
                            d.SetVelocidadeX(d.GetVelocidadeX() - (v1_d / d.getMassa()));
                            d.SetVelocidadeY(d.GetVelocidadeY() - (v2_d / d.getMassa()));
                            d.SetVelocidadeZ(d.GetVelocidadeZ() - (v3_d / d.getMassa()));
    
                        }
                    }
                }
            }
        };
    }

    private double Distancia(Corpo one, Corpo two) {
        double x = one.getTranslateX() - two.getTranslateX();
        double y = one.getTranslateY() - two.getTranslateY();
        return Math.sqrt((x * x) + (y * y));
    }

    private boolean colisao(Corpo one, Corpo two) {
        return one.getBoundsInParent().intersects(two.getBoundsInParent());
    }
}

