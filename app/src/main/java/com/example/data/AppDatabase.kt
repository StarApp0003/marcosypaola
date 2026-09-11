package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [MemoryEntity::class, LoveNoteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
    abstract fun loveNoteDao(): LoveNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "marcos_paola_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed initial memories and notes in coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                seedInitialData(database)
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            val calJuly4 = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.JULY)
                set(Calendar.DAY_OF_MONTH, 4)
                set(Calendar.HOUR_OF_DAY, 18)
                set(Calendar.MINUTE, 30)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val calJuly18 = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.JULY)
                set(Calendar.DAY_OF_MONTH, 18)
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val calAug15 = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.AUGUST)
                set(Calendar.DAY_OF_MONTH, 15)
                set(Calendar.HOUR_OF_DAY, 21)
                set(Calendar.MINUTE, 30)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val calSep4 = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.SEPTEMBER)
                set(Calendar.DAY_OF_MONTH, 4)
                set(Calendar.HOUR_OF_DAY, 19)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val memory0 = MemoryEntity(
                title = "El Día que nos Conocimos",
                description = "El 4 de julio de 2026 nuestras vidas se cruzaron para siempre. Fue el comienzo del capítulo más hermoso de nuestras vidas.",
                dateMillis = calJuly4.timeInMillis,
                mediaUri = "res:img_couple_hero",
                mediaType = "PHOTO",
                isFavorite = true,
                category = "Especial",
                romanticQuote = "Desde el primer instante supe que eras tú, Paola."
            )

            val memory1 = MemoryEntity(
                title = "Nuestra Primera Cita",
                description = "Caminamos por horas juntos y el tiempo voló como si nos conociéramos de toda la vida. Entre risas nerviosas supimos que esto era único.",
                dateMillis = calJuly18.timeInMillis,
                mediaUri = "res:img_memory_date",
                mediaType = "PHOTO",
                isFavorite = true,
                category = "Cita Romántica",
                romanticQuote = "Contigo todo es más bonito."
            )

            val memory2 = MemoryEntity(
                title = "Noche Bajo las Estrellas",
                description = "Abrazados bajo las luces de la noche y el cielo estrellado, soñando con todo nuestro futuro juntos.",
                dateMillis = calAug15.timeInMillis,
                mediaUri = "res:img_couple_hero",
                mediaType = "PHOTO",
                isFavorite = true,
                category = "Especial",
                romanticQuote = "Mi lugar favorito en el mundo es a tu lado."
            )

            val memory3 = MemoryEntity(
                title = "Nuestra Promesa & 2 Meses",
                description = "Cumpliendo 2 meses desde que nos conocimos aquel 4 de julio. Una cartita que guarda todo mi amor y cariño sincero.",
                dateMillis = calSep4.timeInMillis,
                mediaUri = "res:img_love_letter",
                mediaType = "PHOTO",
                isFavorite = true,
                category = "Aniversario",
                romanticQuote = "Te amo hoy más que ayer y menos que mañana."
            )

            database.memoryDao().insertMemory(memory0)
            database.memoryDao().insertMemory(memory1)
            database.memoryDao().insertMemory(memory2)
            database.memoryDao().insertMemory(memory3)

            // Seed love notes
            database.loveNoteDao().insertNote(
                LoveNoteEntity(
                    sender = "Marcos",
                    title = "Tu Sonrisa Mágica",
                    content = "Paola, amo cómo se iluminan tus ojos cada vez que te ríes. Haces que cualquier día difícil se convierta en una bendición.",
                    emoji = "🌹"
                )
            )
            database.loveNoteDao().insertNote(
                LoveNoteEntity(
                    sender = "Paola",
                    title = "Mi Refugio Seguro",
                    content = "Marcos, amo la paz infinita que me dan tus abrazos. Gracias por cuidarme, escucharme y hacerme sentir tan amada siempre.",
                    emoji = "💖"
                )
            )
            database.loveNoteDao().insertNote(
                LoveNoteEntity(
                    sender = "Juntos",
                    title = "Nuestra Canción y Sueños",
                    content = "Cada viaje, cada plan y cada meta compartida es una aventura que solo quiero vivir contigo.",
                    emoji = "✨"
                )
            )
        }
    }
}
