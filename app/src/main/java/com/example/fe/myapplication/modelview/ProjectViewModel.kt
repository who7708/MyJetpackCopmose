import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fe.myapplication.model.Project
import com.example.fe.myapplication.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class ProjectViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    fun getProjectStream(name: String, location: String): Flow<PagingData<Project>> {
        return projectRepository.getProjectStream(name, location).cachedIn(viewModelScope)
    }
}
