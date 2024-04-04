package ind.pali.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Shoppingitem(val id:Int,
                        var name: String,
                        var quantity: Int,
                        var isediting: Boolean = false
)


@Composable
fun Shoppinglistapp()
{
    var sitems by remember { mutableStateOf(listOf<Shoppingitem>()) }
    var showdialog by remember { mutableStateOf(false) }
    var itemname by remember { mutableStateOf("") }
    var itemquantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { showdialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(text = "Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp))
        {
            items(sitems)
            {
                item ->
                if(item.isediting)
                {
                    Shoppinglisteditor(
                        item = item,
                        onedit = {
                            editname, editquantity ->
                            sitems = sitems.map { it.copy(isediting = false) }
                            val editeditem = sitems.find { it.id == item.id }
                            editeditem?.let {
                                it.name = editname
                                it.quantity = editquantity
                            }
                        }
                    )
                }else{
                    Shoppinglistitem(item = item, onedit = {
                        sitems = sitems.map { it.copy (isediting = it.id == item.id) }
                    }, ondelete = {
                        sitems = sitems - item
                    })
                }
            }
        }
    }

    if(showdialog)
    {
        AlertDialog(onDismissRequest = { showdialog=false },
                confirmButton = {
                                Row (modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Button(onClick = {
                                        if(itemname.isNotBlank())
                                        {
                                            val newitem = Shoppingitem(
                                                id = sitems.size+1,
                                                name = itemname,
                                                quantity = itemquantity.toInt()
                                            )
                                            sitems += newitem
                                            showdialog = false
                                            itemname = ""
                                            itemquantity = ""
                                        }
                                    }) {
                                        Text(text = "Add")
                                    }
                                    Button(onClick = { showdialog = false }) {
                                        Text(text = "Cancel")
                                    }
                                }
                },
                title = { Text(text = "Add Shopping Item")},
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemname,
                            label = { Text(text = "Enter Name")},
                            onValueChange = {itemname = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            value = itemquantity,
                            label = { Text(text = "Enter Quantity")},
                            onValueChange = {itemquantity = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
        )
    }
}

@Composable
fun Shoppinglisteditor(item: Shoppingitem, onedit: (String, Int) -> Unit)
{
    var editname by remember { mutableStateOf(item.name) }
    var editquantity by remember { mutableStateOf(item.quantity.toString()) }
    var isediting by remember { mutableStateOf(item.isediting) }

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(
                value = editname,
                onValueChange = { editname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
            )
            BasicTextField(
                value = editquantity,
                onValueChange = {editquantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
            )
        }
        Button(onClick = {
            isediting = false
            onedit(editname,editquantity.toIntOrNull() ?: 1)
            }
        ){
            Text(text = "Save")
        }
    }
}

@Composable
fun Shoppinglistitem(
    item: Shoppingitem,
    onedit: () -> Unit,
    ondelete: () -> Unit
){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty= ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onedit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = ondelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
