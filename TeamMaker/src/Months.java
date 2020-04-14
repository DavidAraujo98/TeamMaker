import java.util.Calendar;

public enum Months {

    JANEIRO("Janeiro", 31), FEVEREIRO("Fevereiro", 28), MARCO("Marco", 31), ABRIL("Abril", 30), MAIO("Maio", 31),
    JUNHO("Junho", 30), JULHO("Julho", 31), AGOSTO("Agosto", 31), SETEMBRO("Setembro", 30), OUTUBRO("Outubro", 31),
    NOVEMBRO("Novembro", 30), DEZEMBRO("Dezembro", 31);

    private String x = null;
    private int y = 0;

    public String getName() {
        return this.x;
    }

    public int getDays() {
        return this.y;
    }

    public static String get(int x) {
        return Months.values()[x].getName();
    }

    private Months(String x, int y) {
        this.x = x;
        int z = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if (z == 366 && x.equals("Fevereiro")) {
            this.y = y + 1;
        } else {
            this.y = y;
        }
    }
}