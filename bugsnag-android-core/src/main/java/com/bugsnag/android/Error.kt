package com.bugsnag.android

class Error @JvmOverloads internal constructor(
    var errorClass: String,
    var errorMessage: String?,
    var stacktrace: List<Stackframe>,
    var type: Type = Type.Android
): JsonStream.Streamable {

    enum class Type(internal val desc: String) {
        Android("android"),
        BrowserJs("browserjs"),
        C("c")
    }

    internal companion object {
        fun createError(exc: Throwable, projectPackages: Collection<String>, logger: Logger): List<Error> {
            val errors = mutableListOf<Error>()

            var currentEx: Throwable? = exc
            while (currentEx != null) {
                val trace = Stacktrace(currentEx.stackTrace, projectPackages, logger)
                errors.add(Error(currentEx.javaClass.name, currentEx.localizedMessage, trace.trace))
                currentEx = currentEx.cause
            }
            return errors
        }
    }

    override fun toStream(writer: JsonStream) {
        writer.beginObject()
        writer.name("errorClass").value(errorClass)
        writer.name("message").value(errorMessage)
        writer.name("type").value(type.desc)
        writer.name("stacktrace").value(stacktrace)
        writer.endObject()
    }
}
