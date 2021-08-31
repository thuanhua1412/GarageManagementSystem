package customer.homepage.service.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dataclasses.Service

@Suppress("UNCHECKED_CAST")
class ServiceDetailViewModelFactory(private var service: Service) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServiceDetailViewModel(service = service) as T
    }
}