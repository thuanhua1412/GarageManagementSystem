package customer.homepage.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dataclasses.Service
import customer.homepage.service.detail.ServiceDetailViewModel

@Suppress("UNCHECKED_CAST")
class ServiceViewModelFactory(private var fromClient: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServiceViewModel(fromClient = fromClient) as T
    }
}