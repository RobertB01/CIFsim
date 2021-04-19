package org.eclipse.escet.common.raildiagrams.solver;

/** An {@link LeRelation} between equality clusters. */
public class LeClusterRelation {
    /** The relation between variables of both clusters (one variable from each). */
    public final LeRelation leRelation;

    /** The cluster with the smaller number (named {@code a}) in the relation. */
    public final EqualityCluster smallerCluster;

    /** The cluster with the bigger number (named {@code b}) in the relation. */
    public final EqualityCluster biggerCluster;

    public LeClusterRelation(LeRelation leRelation, EqualityCluster smallerCluster, EqualityCluster biggerCluster) {
        this.leRelation = leRelation;
        this.smallerCluster = smallerCluster;
        this.biggerCluster = biggerCluster;
    }
}
