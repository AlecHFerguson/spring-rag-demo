package ai.smartalec.ragdemo.model.exception

class MissingPromptException(
    override val message: String = "No prompt provided, please submit a prompt",
) : Exception()
