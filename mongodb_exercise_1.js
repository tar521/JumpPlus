// POPULATE DATA
db.employeeInfo.insertMany([
	{
		empId: 1,
		empFname: "Sanjay",
		empLname: "Mehra",
		department: "HR",
		project: "P1",
		address: {
					city: "Hyderabad(HYD)",
					state: "Telangana"
				},
		dob: {
				month: 1,
				day: 12,
				year: 1976
			},
		gender: "M",
		empPosition: "Manager",
		dateOfJoining: "01/05/2022",
		salary: 500000
	},
	{
		empId: 2,
		empFname: "Ananya",
		empLname: "Mishra",
		department: "Admin",
		project: "P2",
		address: {
					city: "Delhi(DEL)"
				},
		dob: {
				month: 2,
				day: 5,
				year: 1968
			},
		gender: "F",
		empPosition: "Executive",
		dateOfJoining: "02/05/2022",
		salary: 75000
	},
	{
		empId: 3,
		empFname: "Rohan",
		empLname: "Diwan",
		department: "Account",
		project: "P3",
		address: {
					city: "Mumbai(BOM)",
					state: "Maharashtra"
				},
		dob: {
				month: 1,
				day: 1,
				year: 1980
			},
		gender: "M",
		empPosition: "Manager",
		dateOfJoining: "01/05/2022",
		salary: 90000
	},
	{
		empId: 4,
		empFname: "Sonia",
		empLname: "Kulkami",
		department: "HR",
		project: "P1",
		address: {
					city: "Hyderabad(HYD)",
					state: "Telangana"
				},
		dob: {
				month: 2,
				day: 5,
				year: 1992
			},
		gender: "F",
		empPosition: "Lead",
		dateOfJoining: "02/05/2022",
		salary: 85000
	},
	{
		empId: 5,
		empFname: "Ankit",
		empLname: "Kapoor",
		department: "Admin",
		project: "P2",
		address: {
					city: "Delhi(DEL)"
				},
		dob: {
				month: 3,
				day: 7,
				year: 1994
			},
		gender: "M",
		empPosition: "Executive",
		dateOfJoining: "01/05/2022",
		salary: 300000
	}])
	
	
// Exercise 1:
db.employeeInfo.find({empFname: {$exists : true}}, {_id: 0, empFname: 1})

// Exercise 2:
db.employeeInfo.find({department:"HR"}).count()

// Exercise 3:
Date()
	
// Exercise 4:
db.employeeInfo.aggregate([{$project:{_id: 0,firstFourOfLastname: {$substr:["$empLname", 0, 4]}}}])
	
// Exercise 5:
db.employeeInfo.aggregate([
{
	$project: {
		_id:0,
		emplCity: {
			$substr: ["$address.city", 0, {$add: [{$strLenCP: "$address.city"}, -5]}]
		}
	}
}
])
	
// Exercise 6:
db.employeeInfo.find({$and:[{salary: {$gte: 50000}}, {salary: {$lte: 100000}}]})
	
// Exercise 7:
db.employeeInfo.find({empFname: {$regex: /S/}})
	
// Exercise 8:
db.employeeInfo.aggregate([
	{
		$project: {
			_id:0,
			FullName: {
				$concat: ["$empFname", " ", "$empLname"]
			}
		}
	}
])
	
// Exercise 9:
db.employeeInfo.find({}).sort({empLname: -1}, {dapartment: 1})
	
// Exercise 10:
db.employeeInfo.find({$and:[{empFname: {$ne:"Sonia"}},{empFname:{$ne:"Sanjay"}}]})
	
// Exercise 11:
db.employeeInfo.find({"address.city": {$eq:"Delhi(DEL)"}})
	
// Exercise 12:
db.employeeInfo.find({empPosition: {$eq:"Manager"}})
	
// Exercise 13:
db.employeeInfo.aggregate([
	{$group: {_id:"$department", count:{$sum:1}}},
	{$sort: {count:1}}
	
])
	
// Exercise 14:
db.employeeInfo.aggregate([
	{
		$group: {
			_id:"$empPosition", 
			maxSalary: {$max: "$salary"},
			minSalary: {$min: "$salary"}
		}
	}
])
	
// Exercise 15:
db.employeeInfo.aggregate([
    {"$group" : { "_id": "$empFname", "count": { "$sum": 1 } } },
    {"$match": {"_id" :{ "$ne" : null } , "count" : {"$gt": 1} } }, 
    {"$project": {"empFname" : "$_id", "_id" : 0} }
])
	
// Exercise 16:
db.employeeInfo.aggregate([
	{
		$group: {
			_id:"$department",
			employees: {$push: "$$ROOT"}
		}
	}
])
	
// Exercise 17:
db.employeeInfo.aggregate([
	{$sort: {empId:-1}},
	{$limit: 3}
])
	
// Exercise 18:
db.employeeInfo.aggregate([
	{$sort: {salaray:-1}},
	{$skip: 2},
	{$limit: 1}
])
	
// Exercise 19:
db.employeeInfo.aggregate([
	{$group: {
	  _id: null,
	  first: { $first: "$$ROOT" },
	  last: { $last: "$$ROOT" }
	}}
])
	
// Exercise 20:
db.employeeInfo.aggregate([
	{$group: {_id:"$department", count:{$sum:1}}},
	{$match: {"count": {$lt: 2}}}
])
	
// Exercise 21:
db.employeeInfo.aggregate([
	{$group: {_id:"$empPosition", positionSalarySum: {$sum: "$salary"}}}
])
