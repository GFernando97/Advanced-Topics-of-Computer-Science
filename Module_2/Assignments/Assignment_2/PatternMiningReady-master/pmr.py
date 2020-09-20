input_file = open("ratings.dat")
sep = "::"

outfile = open("pmr.txt", "w")

user_movies = {}
seen_users = []

for line in input_file:
	line = line.strip()
	parts = line.split(sep)
	user_id = parts[0]
	movie_id = parts[1]
	if user_id not in seen_users:
		seen_users.append(user_id)
		user_movies[user_id] = []
	user_movies[user_id].append(movie_id)

cnt = 0
for user in seen_users:
	for movie in user_movies[user]:
		outfile.write(movie+" ")
	outfile.write("\n")

outfile.close()
