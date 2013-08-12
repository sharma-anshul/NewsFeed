Instructions

1) Copy config file in home directory and modify the input and output directory accordingly.

2) Run the command: java -Xmx1000m TopicSignatures path_to_home_directory/config.example

This will create a .ts file somewhere in your home directory, depending on how you configured the outputFile field of the configuration file. 
The created file will contain lists of words according to their χ2 scores, where higher scores mean a word is more topical.
