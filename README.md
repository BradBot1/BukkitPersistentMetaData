# BukkitPersistentMetaData

A simple library for persistent MetaData

## How to use

### Create an instance
To create an instance simply do the following <br/>
```
PersistentMetadataHandler h = PersistentMetadataHandler.getPersistentMetadataHandler(); // First try to get an already created instance
if (h==null) { // If this fails
  h = new PersistentMetadataHandler(this, null, null); // Create a new one
}
```
### Saving and loading
Sadly bukkit does not allow for tasks that are only ran when the server stops, this means you will have to call the PersistentMetadataHandler#save(); and PersistentMetadataHandler#load(); methods <br/>
To do this you can simply do the following <br/>
```
@Override
public void onEnable() {
  h.load();
}
	
@Override
public void onDisable() {
  h.save();
}
```
### Custom handlers
Did you know the whole library can be modified easily? </br>
This is due to a couple interfaces that are used, these are: 
<ul>
<li>MetaBlock</li>
<li>MetaSavingHandler</li>
<li>MetaType</li>
</ul>
So what do they do? </br>
Well to start of MetaBlock is very simple, it is the interface that links the Bukkit MetaData to the MetaTypes you use! Its also got the job of handling saving and loading from the block. </br>
Then we have MetaSavingHandler, this one is quite simple as it just handles the saving and loading to/from files or any other methods you wish to save the MetaData in! </br>
Finally we have the most important interface, MetaType! The job of MetaBase is to be able to be saved to blocks while being simple to use </br>
</br>
So then, you know what the interfaces are used for but how do they work and how do you implement them? </br>
Well implementing them is quite simple, do you rememeber when you initilised the PersistentMetadataHandler? Well the nulls you used in the constructor were to say that you wish to use the defaults! If oyu change them they will use your specified version! </br>

```
new PersistentMetadataHandler(Plugin, MetaSavingHandler, GetMetaBlockCallable);
```
When you read that you probally went <i>'What is a GetMetaBlockCallable?'</i> </br>
Well it is quite simple, it is a class that contains a callable method with the parameters Block, Plugin and returns a MetaBlock! This is to make getting MetaBlocks easier and with less exceptions. </br>
So, how do you make one? Just do the following: </br>

```
new GetMetaBlockCallable() {
  @Override
    public MetaBlock getMetaBlock(Block block, Plugin plugin) {
      return new DefaultMetaBlock(block, plugin); // This is where you initilise your version of MetaBlock and return it
    }
  };
}
```
### MetaTypes
So what is a meta type? To put it frankly its a class that stores data that will be saved and retrieved from meta data. </br>
Now that you are interested lets explain how they work, we will be using com.bb1.metatypes.MetaTypeString for this explanation, so feel free to look at how it works along with the other provided MetaTypes. </br>
First you need to create your class, it must implement MetaType (com.bb1.interfaces.MetaType)! </br>
Secondly you will need to add the missing methods, in most IDE's this will be done automatically! </br>
Thirdly you must return values, for the MetaTypeString you can see that we return values that were saved from the constructor, i recommend you do this to allow for more customisable MetaTypes that fit every purpose! </br>
By now you should have noticed the #canBeOverridden(); method, this tells the handler if your data should be able to be changed, however be careful with this as if it is set to false the onlt way to remove it is to clear all of the blocks MetaData with MetaBlock#clear(true); </br>
Finally your class should look like this: </br>

```
public class MetaTypeString implements MetaType {
	
	private static final long serialVersionUID = 136207988888674625L;
	
	private String key;
	private String value;
	
	public MetaTypeString(@NonNull String key, @NonNull String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getMetaTypeName() {
		return "String";
	}

	@Override
	public boolean canBeOverridden() {
		return true;
	}
	
}
```
And yes all data stored in this <b>MUST</b> be serialisable otherwise it will not be able to be saved!
### How to save MetaTypes to blocks
Now if you want to save MetaTypes to the block you can just do the following!

```
// This example expects that you have an instance of PersistentMetadataHandler stored in the variable 'h'

// First get a block, for this example i will be using the block at 4, 4, 4 in world 0
Block block = Bukkit.getWorlds().get(0).getBlockAt(4, 4, 4);
// Then get its MetaBlock counterpart
MetaBlock metaBlock = h.getMetaBlockOf(block);
// Then we need to get an instance of a MetaType to save to the block, for this example we will use MetaTypeString
MetaType metaType = new MetaTypeString("RandomKeyName101", "SuperCoolResponce!");
// After this we need to add it to the MetaBlock
metaBlock.addMetaType(metaType);
// Then we need to save it to the block
metaBlock.updateToBlock();
// Now to get data from the block, before you do this make sure you update from the block incase the data has changed on the block!
metaBlock.updateFromBlock();
// Once you have done that just get the data based on the key you set earlier!
String data = metaBlock.getMetaData("RandomKeyName101").getValue();
// And then you can do what ever you wanted with the data!
```
### What can I expect to come to this library?
Alot! First I'm planning on working on Persistent MetaData for entities and players, then my next aim is to allow MetaTypes to be saved to items via NBT!
