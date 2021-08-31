package notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.R
import com.example.dataclasses.Notification

class NotificationRecyclerViewAdapter(
    private val notificationList: List<Notification>,
    private val listener: NotificationItemClickListener
) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemViewHolder {
        val notificationObject = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationItemViewHolder(notificationObject)
    }

    override fun onBindViewHolder(holder: NotificationItemViewHolder, position: Int) {
        val currentItem = notificationList[position]
        holder.notificationTitle.text = currentItem.title
        holder.notificationMainText.text = currentItem.mainInformation
        holder.dateCreated.text = currentItem.dateCreated
        holder.description.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class NotificationItemViewHolder(NotificationItem: View) :
        RecyclerView.ViewHolder(NotificationItem), View.OnClickListener {
        var notificationTitle : TextView = NotificationItem.findViewById(R.id.notification_title)
        var notificationMainText : TextView = NotificationItem.findViewById(R.id.notification_main_text)
        var dateCreated : TextView = NotificationItem.findViewById(R.id.notification_date_created)
        var description : TextView = NotificationItem.findViewById(R.id.notification_description)


        init {
            NotificationItem.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onNotificationItemClicked(position)
            }
        }

    }

    interface NotificationItemClickListener {
        fun onNotificationItemClicked(position: Int)
    }

}