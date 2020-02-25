package it.unibo.alchemist.model.implementations.conditions

import it.unibo.alchemist.model.implementations.actions.RunProtelisProgram
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode
import it.unibo.alchemist.model.interfaces.Context
import it.unibo.alchemist.model.interfaces.Dependency
import it.unibo.alchemist.model.interfaces.Environment

class NewMessageReceivedOrPositionChanged(
    val environment: Environment<Any, *>,
    val node: ProtelisNode<*>
): ChangeDetectionCondition<Any, Any>(node) {

    init {
        declareDependencyOn(Dependency.EVERYTHING)
    }

    override val currentValue: Any
        get() = listOf(
            environment.getPosition(node),
            node.getReactions().asSequence()
                .flatMap { it.actions.asSequence() }
                .filter { it is RunProtelisProgram<*> }
                .map { it as RunProtelisProgram<*> }
                .map { node.getNetworkManager(it) }
                .map { it.neighborState }
                .toList()
        )

    /**
     * @return The context for this condition.
     */
    override fun getContext() = Context.LOCAL

}
