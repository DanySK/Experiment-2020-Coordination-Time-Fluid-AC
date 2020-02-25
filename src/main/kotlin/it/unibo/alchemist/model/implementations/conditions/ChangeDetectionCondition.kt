package it.unibo.alchemist.model.implementations.conditions

import it.unibo.alchemist.model.interfaces.Node

abstract class ChangeDetectionCondition<T, X>(
    node: Node<T>
) : AbstractCondition<T>(node) {

    private var previousValue: X? = null
    private var hasFlipped = false

    protected open val propensityContributionWhenValid: Double = 1.0

    abstract val currentValue: X?

    final override fun getPropensityContribution() = if (isValid) propensityContributionWhenValid else 0.0

//        override final fun getPropensityContribution(): Double {
//        if (!hasFlipped && currentValue != previousValue) {
//            hasFlipped = true
//            previousValue = currentValue
//        }
//        return if(hasFlipped) propensityContributionWhenValid else 0.0
//    }

    final override fun isValid(): Boolean {
        if (!hasFlipped) {
            hasFlipped = previousValue != currentValue
        }
        return hasFlipped
    }

    override fun reactionReady() {
        hasFlipped = false
        previousValue = currentValue
    }

    override fun toString() = javaClass.simpleName + "[$hasFlipped]"

}