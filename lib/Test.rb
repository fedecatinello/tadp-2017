require_relative 'modules/CaseClassModule.rb'
require_relative 'CaseClass.rb'

class Test
	
	def test
		t1 = Test.new
		t1.case_class B do end
	end

end
