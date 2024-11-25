package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Corpo extends Sphere {
    private int id;
    private Double massa;
    private double x;
    private double y;
    private double Z;
    private double densidade;
    private double volume;
    private String nome;
    private double raio;
    private double velocidadeX;
    private double velocidadeY;
    private double velocidadeZ;
    private PhongMaterial material = new PhongMaterial();
    private String cor;
    private Relatorio relatorio;

    public Double getMassa() {
        return massa;
    }

    public void setMassa(Double massa) {
        this.massa = massa;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDensidade() {
        return densidade;
    }

    public void setDensidade(double densidade) {
        this.densidade = densidade;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getNome() {
        return nome;
    }

    public double GetVelocidadeX() {
        return velocidadeX;
    }

    public double GetVelocidadeY() {
        return velocidadeY;
    }

    public void SetVelocidadeX(double velocidadex) {
        this.velocidadeX = velocidadex;
    }

    public void SetVelocidadeY(double velocidadey) {
        this.velocidadeY = velocidadey;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    

    public Corpo(Double massa, String nome, double raio, double x, double y, double z, double velocidadex,
            double velociadey, double velociadez) {
        this.x = x;
        this.setTranslateX(x);
        this.y = y;
        this.setTranslateY(y);
        this.Z = z;
        this.setTranslateZ(z);
        this.massa = massa;
        this.nome = nome;
        this.raio = raio;
        this.setRadius(raio);
        this.velocidadeX = velocidadex;
        this.velocidadeY = velociadey;
        this.velocidadeZ = velociadez;
        volume = (float) ((4 / 3) * 3.14 * (raio * raio * raio));
        densidade = (float) (massa / volume);
        id = -1;
        this.relatorio = new Relatorio(this);
    }

    public Corpo() {

    }

    public void Colorir(Color cor) {
        if (cor == null) {
            material.setDiffuseColor(Color.WHITE);
            this.cor = Color.WHITE.toString();
        } else {
            material.setDiffuseColor(cor);
            this.cor = cor.toString(); 
        }
        this.setMaterial(material);
    }
    
    
    public void ColorirCorSalva(String corSalva) {
        try {
            System.out.println(corSalva);
            Color color = Color.web(corSalva); 
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(color);
            this.setMaterial(material); 
        } catch (IllegalArgumentException e) {
            System.out.println("Formato de cor inválido: " + corSalva);
        }
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int GetId() {
        return id;
    }

    public double GetVelocidadeZ() {
        return velocidadeZ;
    }

    public void Texturizar(String local) {
        material.setDiffuseMap(new Image(getClass().getResourceAsStream(local)));
        this.setMaterial(material);
    }

    public void SetVelocidadeZ(double velocidadeZ) {
        this.velocidadeZ = velocidadeZ;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }

    public double getVelocidadeX() {
        return velocidadeX;
    }

    public void setVelocidadeX(double velocidadeX) {
        this.velocidadeX = velocidadeX;
    }

    public double getVelocidadeY() {
        return velocidadeY;
    }

    public void setVelocidadeY(double velocidadeY) {
        this.velocidadeY = velocidadeY;
    }

    public double getVelocidadeZ() {
        return velocidadeZ;
    }

    public void setVelocidadeZ(double velocidadeZ) {
        this.velocidadeZ = velocidadeZ;
    }

    public void setMaterial(PhongMaterial material) {
        this.material = material;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }


    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }
}
