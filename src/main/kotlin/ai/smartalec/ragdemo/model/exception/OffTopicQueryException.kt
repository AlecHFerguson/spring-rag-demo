package ai.smartalec.ragdemo.model.exception

class OffTopicQueryException(
    override val message: String = "Query is off topic. Please only make requests pertaining to topics this agent is trained on.",
) : Exception()
