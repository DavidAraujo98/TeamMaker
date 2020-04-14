import java.util.ArrayList;
import java.util.Collections;

class Team implements Comparable<Team>{
    private static int id = 1;
    private static ArrayList<Team> team_db = new ArrayList<Team>();
    private int pid = 0;
    private ArrayList<Firefighter> tm = new ArrayList<Firefighter>();
    private static int pesado = 1, ligeiro = 2, tas = 2, minestg = 1, maxestg =2, teamnum = 5;

    public Team() {
        this.pid = id;
        id++;
        team_db.add(this);
    }

    public static ArrayList<Team> getAll() {
        return team_db;
    }

    public static void remove(int d) {
        team_db.remove(d);
    }

    public static void clear() {
        team_db.clear();
    }

    public static void clear(Firefighter x) {
        team_db.remove(x);
    }

    public static int getNumberOfTeams() {
        return teamnum;
    }

    public static void setNumberOfTeams(int number) {
        teamnum = number;
    }

    public static void setMinimums(int ps, int lig, int tass, int mestg, int Mestg) {
        pesado = ps;
        ligeiro = lig;
        tas = tass;
        minestg = mestg;
        maxestg = Mestg;
    }

    public boolean getTruckDrivers() {
        int x = 0;
        for (int i = 0; i < this.tm.size(); i++) {
            if (this.tm.get(i).getPesado()) {
                x++;
            }
        }
        return (x >= pesado ? true : false);
    }

    public boolean getAmbulanceDrivers() {
        int x = 0;
        for (int i = 0; i < this.tm.size(); i++) {
            if (this.tm.get(i).getLigeiro()) {
                x++;
            }
        }
        return (x >= ligeiro ? true : false);
    }

    public boolean getTas() {
        int x = 0;
        for (int i = 0; i < this.tm.size(); i++) {
            if (this.tm.get(i).getTas()) {
                x++;
            }
        }
        return (x >= tas ? true : false);
    }

    public boolean getEstg() {
        int x = 0;
        for (int i = 0; i < this.tm.size(); i++) {
            if (this.tm.get(i).getGrad() == Graduacao.ESTAGIARIO) {
                x++;
            }
        }
        return ((x >= minestg && x <= maxestg) ? true : false);
    }

    public Firefighter get(int i) {
        return this.tm.get(i);
    }

    public int getId() {
        return this.pid;
    }

    public ArrayList<Firefighter> getTm() {
        Collections.sort(tm);
        return this.tm;
    }

    public int size() {
        return this.tm.size();
    }

    public boolean add(Firefighter x) {
        tm.add(x);
        x.setTeam(id);
        return true;
    }

    public boolean rm(Firefighter x) {
        tm.remove(x);
        return true;
    }

    @Override
    public int compareTo(Team o) {
        return this.pid - o.getId();
    }

    @Override
    public String toString() {
        return "{" + " tm='" + getTm() + "'" + "}";
    }
}