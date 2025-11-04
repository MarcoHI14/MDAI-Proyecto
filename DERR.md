classDiagram
direction BT
class Artista {
  + addCancion(Cancion) void
  + removeCancion(Cancion) void
   List~Cancion~ canciones
   Long idArtista
   String biografia
   Usuario usuario
}
class Cancion {
  + removeCancionPlaylist(CancionPlaylist) void
  + addCancionPlaylist(CancionPlaylist) void
   Long idCancion
   LocalDateTime fechaSubida
   Artista artista
   String archivoAudio
   String titulo
   String genero
   String duracion
   List~CancionPlaylist~ cancionPlaylists
}
class CancionPlaylist {
  - postRemove() void
  - postPersist() void
   Playlist playlist
   int orden
   Long id
   Cancion cancion
}
class Playlist {
  + addCancionPlaylist(CancionPlaylist) void
  + removeCancionPlaylist(CancionPlaylist) void
   String descripcion
   Long idPlaylist
   List~CancionPlaylist~ cancionPlaylists
   LocalDateTime fechaCreacion
   Usuario usuario
   String nombre
}
class SolicitudVerificacion {
   String mensajeVerificacion
   String estado
   Usuario usuario
   Long idSolicitud
   LocalDateTime fechaSolicitud
}
class Usuario {
  + addSolicitud(SolicitudVerificacion) void
  + comprobarPassword(String) boolean
  + removeSolicitud(SolicitudVerificacion) void
   List~SolicitudVerificacion~ solicitudes
   String password
   Artista artista
   String username
   Long id
   String email
}

Artista "1" *--> "canciones *" Cancion 
Artista "1" *--> "usuario 1" Usuario 
Cancion "1" *--> "artista 1" Artista 
Cancion "1" *--> "cancionPlaylists *" CancionPlaylist 
CancionPlaylist "1" *--> "cancion 1" Cancion 
CancionPlaylist "1" *--> "playlist 1" Playlist 
Playlist "1" *--> "cancionPlaylists *" CancionPlaylist 
Playlist "1" *--> "usuario 1" Usuario 
SolicitudVerificacion "1" *--> "usuario 1" Usuario 
Usuario "1" *--> "artista 1" Artista 
Usuario "1" *--> "solicitudes *" SolicitudVerificacion 
