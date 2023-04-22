class DirectoryTypes {
  static int downloads = 0;
  static int documents = 1;
  static int pictures = 2;
  static int movies = 3;
  static int screenshots = 4;
  static int music = 5;
  static int recordings = 6;

  static int getType({required DirectoryType dirType}){
    return switch(dirType){
      DirectoryType.downloads => downloads,
      DirectoryType.documents => documents,
      DirectoryType.pictures => pictures,
      DirectoryType.movies => movies,
      DirectoryType.screenshots => screenshots,
      DirectoryType.music => music,
      DirectoryType.recordings => recordings
    };
  }
}


enum DirectoryType {
  downloads, 
  documents, 
  pictures, 
  movies, 
  screenshots, 
  music, 
  recordings
}