package com.rafal.firestoretodo.di


import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirestoreDb() = FirebaseFirestore.getInstance()


    @Named("todos_collection")
    @Provides
    @Singleton
    fun provideFirestoreToDoCollection(db: FirebaseFirestore) = db.collection("todos")

}