package primitives;

public class Material {
    public Double3 kA = Double3.ONE; // Ambient reflection coefficient
    public Double3 kD = Double3.ZERO; // Diffuse reflection coefficient
    public Double3 kS = Double3.ZERO; // Specular reflection coefficient
    public int nShininess = 0; // Shininess exponent for specular reflection

    public Material setKa(Double3 kA) {
        this.kA = kA;
        return this;
    }
    public Material setKa(double kA) {
        return setKa(new Double3(kA));
    }
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }
    public Material setKd(double kD) {
        return setKd(new Double3(kD));
    }
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }
    public Material setKs(double kS) {
        return setKs(new Double3(kS));
    }
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
