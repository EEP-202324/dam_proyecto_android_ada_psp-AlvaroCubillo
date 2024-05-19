import com.example.aplicacion.model.Task
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Call;

interface ApiService {
    @GET("/tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("/savetask")
    suspend fun createTask(@Body task: Task): Response<Task>

    @PUT("/update/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: Task): Response<Task>

    @DELETE("/delete/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Unit>
}
