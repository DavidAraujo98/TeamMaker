import java.util.ArrayList;
import java.util.Collections;

class Firefighter implements Comparable<Firefighter> {
    private static int id = 1;
    private static ArrayList<Firefighter> firefighter_db = new ArrayList<Firefighter>();
    private int pid = 0;
    private String name = null;
    private int n_mec = 0;
    private Graduacao grad = null;
    private boolean tas = false;
    private boolean ligeiro = false;
    private boolean pesado = false;
    private Quadro quadro = null;
    private Estado estado = null;
    private int team = 0;

    public Firefighter(String name, int mec, Graduacao grad, boolean tas, boolean lig, boolean pes, Quadro quadro,
            Estado estado) {
        this.name = name;
        this.n_mec = mec;
        this.grad = grad;
        this.tas = tas;
        this.ligeiro = lig;
        this.pesado = pes;
        this.quadro = quadro;
        this.estado = estado;
        this.pid = id;
        Firefighter.id++;
        if (!firefighter_db.contains(this)) {
            firefighter_db.add(this); 
        }
        Collections.sort(firefighter_db);
    }

    public static ArrayList<Firefighter> getAll() {
        return firefighter_db;
    }

    public static void clear() {
        firefighter_db.clear();
    }

    public static void clear(Firefighter x){
        firefighter_db.remove(x);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getN_mec() {
        return this.n_mec;
    }

    public void setN_mec(int n_mec) {
        this.n_mec = n_mec;
    }

    public Graduacao getGrad() {
        return this.grad;
    }

    public void setTeam(int x) {
        this.team = x;
    }

    public int getTeam() {
        return this.team;
    }

    public void setGrad(Graduacao grad) {
        this.grad = grad;
    }

    public boolean getTas() {
        return this.tas;
    }

    public void setTas(boolean tas) {
        this.tas = tas;
    }

    public boolean getLigeiro() {
        return this.ligeiro;
    }

    public void setLigeiro(boolean ligeiro) {
        this.ligeiro = ligeiro;
    }

    public boolean getPesado() {
        return this.pesado;
    }

    public void setPesado(boolean pesado) {
        this.pesado = pesado;
    }

    public int getId() {
        return this.pid;
    }

    public Quadro getQuadro() {
        return this.quadro;
    }

    public void setQuadro(Quadro quadro) {
        this.quadro = quadro;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    // Compare graduation
    @Override
    public int compareTo(Firefighter o) {
        int y = ((Firefighter) o).getGrad().ordinal();
        return this.getGrad().ordinal()-y;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", n_mec='" + getN_mec() + "'"
                + ", grad='" + getGrad() + "'" + ", tas='" + getTas() + "'" + ", ligeiro='" + getLigeiro() + "'"
                + ", pesado='" + getPesado() + "'" + ", quadro='" + getQuadro().getValue() + "'" + ", estado='"
                + getEstado().getValue() + "'" + "}";
    }

}