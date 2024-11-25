package model;

public class CorposController {
    public static final double G = 120e-2;
    public void atualizarVelocidades(Corpo one, Corpo two) {
        double dx = two.getTranslateX() - one.getTranslateX();
        double dy = two.getTranslateY() - one.getTranslateY();
        double dz = two.getTranslateZ() - one.getTranslateZ();
        double distancia = Distancia(one, two);
    
        if (distancia < 1e-10) return; 

        double nx = dx / distancia;
        double ny = dy / distancia;
        double nz = dz / distancia;
    
        double forca = (one.getMassa() * two.getMassa()) * G / (distancia * distancia);
    
        double aceleracao_one = forca / one.getMassa();
        double aceleracao_two = forca / two.getMassa();
    
        one.SetVelocidadeX(one.GetVelocidadeX() + aceleracao_one * nx);
        one.SetVelocidadeY(one.GetVelocidadeY() + aceleracao_one * ny);
        one.SetVelocidadeZ(one.GetVelocidadeZ() + aceleracao_one * nz);
    
        two.SetVelocidadeX(two.GetVelocidadeX() - aceleracao_two * nx);
        two.SetVelocidadeY(two.GetVelocidadeY() - aceleracao_two * ny);
        two.SetVelocidadeZ(two.GetVelocidadeZ() - aceleracao_two * nz);
    }
    
    private double Distancia(Corpo one, Corpo two) {
        double dx = one.getTranslateX() - two.getTranslateX();
        double dy = one.getTranslateY() - two.getTranslateY();
        double dz = one.getTranslateZ() - two.getTranslateZ();
    
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }
}
