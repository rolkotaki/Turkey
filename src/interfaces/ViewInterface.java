
package interfaces;

import java.text.SimpleDateFormat;

/**
 * Ez az interfész tartalmazza a grafikus interfészek komponensei számára a
 * konstansokat. Legfőképpen a JTable komponensek oszlopazonosítói találhatók
 * meg itt.
 */

public interface ViewInterface {

    public static final String[] TURKEYBASIC = 
            {"Dátum","Nap","Életkor","Hét","Darabszám\n(bak)","Darabszám\n(tojó)","Elhalálozás\n(bak)","Elhalálozás\n(tojó)",
                "Selejtezés\n(bak)","Selejtezés\n(tojó)","Elhullás\nösszesen"};
    public static final String[] TURKEYCONSUMPTION = 
            {"Dátum","Előírt\ntakarmány\n(kg/db/hét)","Előírt\ntakarmány\n(kg/db/nap)","Takarmány\nkivitel\n(zsák)",
                "Takarmány\nkivitel\n(kg)","Összes\nkivitt\ntakarmány\n(kg)","Napi\nelőírt\nösszes\ntakarmány",
                "Napi\nelőírt\ntakarmány\nösszesítve","Heti\nelőírt\nfogyasztás","Heti\nvalós\nfogyasztás","Heti\neltérés",
                "Heti\nelőírt\nfogyasztás\nösszesítve","Heti\nvalós\nfogyasztás\nösszesítve","Heti\nösszesített\neltérés"};
    public static final String[] TURKEYBUCKHEN = 
            {"Dátum","Napi\ndarabszám","Napi\nelhullás","Összes\nelhullás","Takarmány\nkivitel\n(kg)","Takarmány\nkivitel\nösszesítve",
                "Napi\nelőírt\nfogyasztás","Napi\nelőírt\nhalmozott\nfogyasztás","Heti\nelőírt\nfogyasztás","Heti\nvalós\nfogyasztás",
                "Heti\neltérés","Heti\nelőírt\nfogyasztás\nösszesítve","Heti\nvalós\nfogyasztás\nösszesítve","Heti\nösszesített\neltérés"};
    public static final String[] TURKEYSTOCK = 
            {"Dátum","Raktárkészlet (kg)","Takarmányrendelés\n(kg)","Takarmányfogadás\n(kg)","Szalmabála\n(bak)","Szalmabála\n(tojó)",
                "Szalmabála\nösszesen"};
    public static final String[] TURKEYOTHER = 
            {"Dátum","Gyógyszer","Technikai események","Árváltozás"};
    public static final String[] TURKEYWEIGHING = 
            {"Hét","Bak\n(db)","Bak\n(hízás)","Bak\n(összes)","Bak\n(összhízás)","Tojó\n(db)","Tojó\n(hízás)",
                "Tojó\n(összes)","Tojó\n(összhízás)","Átlagos\n(db)","Átlagos\n(hízás)","Átlagos\n(összes)",
                "Átlagos\n(összhízás)"};
    public static final String[] WEEK = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17"};
    public static final String[] BUCKHEN = {"bak","tojó"};
    public static final String[] INDEXNUMBER = {"Hét", "Bakok", "Tojók", "Együttes"};
    public static final String[] CBLIST = {"minden sor","összegzés","átlag","minimum","maximum"};
    public static final String[] CBOTHER = {"gyógyszer","technikai események","árváltozás"};
    public static final String[] CBTURNUMCHANGE = {"bak --> tojó","tojó --> bak"};
    public static final String[] COL_IDENTIFIERS ={"Összeg","Átlag","Minimum érték","Maximum érték"};
}
