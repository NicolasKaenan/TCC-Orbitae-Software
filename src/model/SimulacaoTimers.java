package model;
import javafx.animation.AnimationTimer;
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
                        corposController.atualizarPosicao(d, c);
                        System.out.println("Distancia entre " + c.getNome() + " e " + d.getNome() + " "+ Distancia(c, d));
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
                            // Vetor da diferença de posição entre os corpos c e d
                            double dx = c.getTranslateX() - d.getTranslateX();
                            double dy = c.getTranslateY() - d.getTranslateY();
                            double dz = c.getTranslateZ() - d.getTranslateZ();
                            
                            // Distância ao quadrado entre os corpos c e d
                            double dist2 = dx * dx + dy * dy + dz * dz;
    
                            // Produto escalar da diferença de velocidade
                            double dvx = c.GetVelocidadeX() - d.GetVelocidadeX();
                            double dvy = c.GetVelocidadeY() - d.GetVelocidadeY();
                            double dvz = c.GetVelocidadeZ() - d.GetVelocidadeZ();
                            
                            double dvDotDx = dvx * dx + dvy * dy + dvz * dz; // Produto escalar das velocidades
    
                            // Massas dos corpos
                            double m1 = c.getMassa();
                            double m2 = d.getMassa();
    
                            // Fórmula da colisão elástica
                            double fator = 2 * m2 / (m1 + m2) * (dvDotDx / dist2);
    
                            // Atualiza as velocidades de c
                            c.SetVelocidadeX(c.GetVelocidadeX() - fator * dx);
                            c.SetVelocidadeY(c.GetVelocidadeY() - fator * dy);
                            c.SetVelocidadeZ(c.GetVelocidadeZ() - fator * dz);
    
                            // Atualiza as velocidades de d (simetria)
                            fator = 2 * m1 / (m1 + m2) * (dvDotDx / dist2);
                            d.SetVelocidadeX(d.GetVelocidadeX() + fator * dx);
                            d.SetVelocidadeY(d.GetVelocidadeY() + fator * dy);
                            d.SetVelocidadeZ(d.GetVelocidadeZ() + fator * dz);

                            c.getRelatorio().somarQuantidadeColisoes();
                            d.getRelatorio().somarQuantidadeColisoes();
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
        double dx = one.getTranslateX() - two.getTranslateX();
        double dy = one.getTranslateY() - two.getTranslateY();
        double dz = one.getTranslateZ() - two.getTranslateZ();
        
        double distancia = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double somaRaios = one.getRaio() + two.getRaio(); // Supondo que os corpos têm um método getRaio()
        
        return distancia < somaRaios;
    }
    
}

