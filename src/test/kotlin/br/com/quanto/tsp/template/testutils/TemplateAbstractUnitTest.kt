package br.com.quanto.tsp.template.testutils

import io.mockk.impl.annotations.MockK
import org.springframework.cloud.sleuth.Span
import org.springframework.cloud.sleuth.TraceContext
import org.springframework.cloud.sleuth.Tracer
import java.util.Random

abstract class TemplateAbstractUnitTest {

    @MockK
    lateinit var tracer: Tracer

    @MockK
    lateinit var span: Span

    @MockK
    lateinit var context: TraceContext

    private fun generateTraceId(): String {
        val r = Random()
        val sb = StringBuffer()
        while (sb.length < 32) {
            sb.append(Integer.toHexString(r.nextInt()))
        }

        return sb.toString().substring(0, 32)
    }
}
