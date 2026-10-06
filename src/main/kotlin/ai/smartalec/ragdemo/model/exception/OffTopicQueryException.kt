package ai.smartalec.ragdemo.model.exception

const val OFF_TOPIC_EXCEPTION_MESSAGE = "Query is off topic. Please only make requests pertaining to topics this agent is trained on."

class OffTopicQueryException(
    override val message: String = OFF_TOPIC_EXCEPTION_MESSAGE,
) : Exception()
