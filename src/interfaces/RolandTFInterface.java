
package interfaces;

/**
 * Ez az interfész a saját komponensem, a RolandTextField osztály számára
 * tartalmazza a leüthető karaktereket és a típusokat leíró konstansokat.
 */

public interface RolandTFInterface {
    public static final String VALIDDOUBLECHARACTERS = "0123456789.";
    public static final String VALIDINTCHARACTERS = "0123456789";
    public static final int TYPEINT = 0;
    public static final int TYPEDOUBLE = 1;    
}
